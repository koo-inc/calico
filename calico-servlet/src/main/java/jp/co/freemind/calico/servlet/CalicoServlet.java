package jp.co.freemind.calico.servlet;

import static com.google.common.base.MoreObjects.firstNonNull;
import static com.google.common.base.Strings.emptyToNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ByteBufferBackedInputStream;
import jp.co.freemind.calico.core.auth.AuthToken;
import jp.co.freemind.calico.core.endpoint.Dispatcher;
import jp.co.freemind.calico.core.endpoint.EndpointResolver;
import jp.co.freemind.calico.core.endpoint.TransactionScoped;
import jp.co.freemind.calico.core.endpoint.aop.InterceptionHandler;
import jp.co.freemind.calico.core.endpoint.interceptor.result.Result;
import jp.co.freemind.calico.core.zone.Zone;
import jp.co.freemind.calico.servlet.assets.Asset;
import jp.co.freemind.calico.servlet.assets.AssetsFinder;
import jp.co.freemind.calico.servlet.assets.AssetsSetting;
import jp.co.freemind.calico.servlet.util.NetworkUtil;
import jp.co.freemind.calico.servlet.util.SessionUtil;
import nu.validator.htmlparser.dom.Dom2Sax;
import nu.validator.htmlparser.dom.HtmlDocumentBuilder;
import nu.validator.htmlparser.sax.HtmlSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@WebServlet(name = "CalicoServlet", urlPatterns = "/*", asyncSupported = true)
public class CalicoServlet extends HttpServlet {

  private EndpointResolver resolver;
  private AssetsSetting assetsSetting;
  private InterceptionHandler[] interceptionHandlers;

  @Override
  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);
    Zone root = Zone.getCurrent();
    this.resolver = root.getInstance(EndpointResolver.class);
    this.assetsSetting = root.getInstance(AssetsSetting.class);
    this.interceptionHandlers = (InterceptionHandler[]) servletConfig.getServletContext()
      .getAttribute(CalicoServletConfig.INTERCEPTOR_HANDLERS);

    ObjectMapper mapper = root.getInstance(ObjectMapper.class)
      .copy().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    Dispatcher.init(mapper);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    String path = req.getPathInfo();
    String remoteAddress = firstNonNull(NetworkUtil.getRemoteAddrWithConsiderForwarded(req), "(unknown)");
    LocalDateTime processDatetime = LocalDateTime.now();
    AuthToken authToken = SessionUtil.getSessionToken(req)
      .map(AuthToken::of)
      .orElseGet(() -> AuthToken.createEmpty(remoteAddress, processDatetime));

    Zone.getCurrent().fork(s -> s
      .scope(TransactionScoped.class)
      .provide(Keys.AUTH_TOKEN, authToken)
      .provide(Keys.PATH, path)
      .provide(Keys.REMOTE_ADDRESS, remoteAddress)
      .provide(Keys.PROCESS_DATETIME, processDatetime)
      .provide(Keys.SERVLET_REQUEST, req)
      .provide(Keys.SERVLET_RESPONSE, res)
      .onError(e -> {
        e.printStackTrace();
        new DefaultRenderer(500).render(null);
      })
    ).run(()-> {
      Object output = new Dispatcher(resolver).dispatch(path, req.getInputStream(), interceptionHandlers);
      if (output instanceof Result) {
        new ResultRenderer().render((Result) output);
      }
      else {
        new DefaultRenderer(200).render(output);
      }
    });
  }

  private String getContextPath() {
    return firstNonNull(emptyToNull(getServletContext().getContextPath()), "/");
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    String path = Paths.get(req.getPathInfo()).normalize().toString();
    if (path.matches("/[^./]*$")) {
      sendIndex(res);
    }
    else {
      sendAsset(res, path);
    }
  }

  private Asset index;
  private void sendIndex(HttpServletResponse res) {
    if (index == null || !assetsSetting.cacheEnabled()) {
      Asset asset = Zone.getCurrent().getInstance(AssetsFinder.class).getAsset(getServletContext(), assetsSetting.getIndex())
        .orElseThrow(()-> new IllegalStateException("'assets.index' setting is invalid."));

      ByteBuffer content = asset.getContent();
      HtmlDocumentBuilder builder = new HtmlDocumentBuilder();
      try (InputStream is = new ByteBufferBackedInputStream(content)) {
        Document document = builder.parse(is);
        NodeList baseTags = document.getElementsByTagName("base");
        if (baseTags.getLength() > 0) {
          for (int i = 0, len = baseTags.getLength(); i < len; i++) {
            Node baseTag = baseTags.item(i);
            Node href = baseTag.getAttributes().getNamedItem("href");
            href.setNodeValue(getContextPath());
          }
        }

        try (ByteArrayOutputStream os = new ByteArrayOutputStream();
             Writer writer = new OutputStreamWriter(os)) {

          HtmlSerializer serializer = new HtmlSerializer(writer);
          Dom2Sax dom2Sax = new Dom2Sax(serializer, serializer);
          dom2Sax.parse(document);

          ByteBuffer byteBuffer = ByteBuffer.allocate(os.size());
          byteBuffer.put(os.toByteArray());
          byteBuffer.compact();

          index = new Asset(byteBuffer.asReadOnlyBuffer(), asset.getContentType(), os.size(), asset.getLastModified());
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      } catch (SAXException e) {
        throw new IllegalStateException(e);
      }
    }

    sendAsset(res, index);
  }

  private void sendAsset(HttpServletResponse res, String path) throws IOException {
    Optional<Asset> asset = Zone.getCurrent().getInstance(AssetsFinder.class).getAsset(getServletContext(), path);
    if (asset.isPresent()) {
      sendAsset(res, asset.get());
    }
    else {
      res.sendError(404);
    }
  }

  private void sendAsset(HttpServletResponse res, Asset asset) {
    res.setStatus(200);
    res.setContentType(asset.getContentType());
    res.setContentLengthLong(asset.getContentLength());
    try (ServletOutputStream os = res.getOutputStream();
         WritableByteChannel channel = Channels.newChannel(os)) {
      channel.write(asset.getContent());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
