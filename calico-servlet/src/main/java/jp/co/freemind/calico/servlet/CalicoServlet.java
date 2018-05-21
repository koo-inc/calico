package jp.co.freemind.calico.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.Optional;
import java.util.regex.Pattern;

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
import jp.co.freemind.calico.core.config.SystemSetting;
import jp.co.freemind.calico.core.endpoint.Dispatcher;
import jp.co.freemind.calico.core.zone.Zone;
import jp.co.freemind.calico.servlet.assets.Asset;
import jp.co.freemind.calico.servlet.assets.AssetsFinder;
import jp.co.freemind.calico.servlet.assets.AssetsSetting;
import jp.co.freemind.calico.servlet.util.NetworkUtil;
import nu.validator.htmlparser.dom.Dom2Sax;
import nu.validator.htmlparser.dom.HtmlDocumentBuilder;
import nu.validator.htmlparser.sax.HtmlSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@WebServlet(name = "CalicoServlet", urlPatterns = "/*", asyncSupported = true)
public class CalicoServlet extends HttpServlet {

  private AssetsSetting assetsSetting;

  @Override
  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);
    Zone root = Zone.getCurrent();
    this.assetsSetting = root.getInstance(AssetsSetting.class);

    ObjectMapper mapper = root.getInstance(ObjectMapper.class)
      .copy().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    Dispatcher.init(mapper);

    root.getInstance(SystemSetting.class).proxies().forEach(NetworkUtil::addProxy);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    RequestSession requestSession = Zone.getCurrent().getInstance(RequestSession.class);
    requestSession.execute(getServletConfig(), req, res);
  }

  private static Pattern INDEX_PATTERN = Pattern.compile("^(?:/[^./]*)+$");

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    String path = URI.create(req.getPathInfo()).normalize().toString();
    if (INDEX_PATTERN.matcher(path).matches()) {
      sendIndex(req, res);
    }
    else {
      sendAsset(res, path);
    }
  }

  private Asset index;
  private String indexHtml;
  private static final String BASE_HREF_PLACEHOLDER = "{{{{ BASE HREF PLACEHOLDER }}}}";
  private void sendIndex(HttpServletRequest req, HttpServletResponse res) {
    if (index == null || !assetsSetting.cacheEnabled()) {
      index = Zone.getCurrent().getInstance(AssetsFinder.class).getAsset(getServletContext(), assetsSetting.getIndex())
        .orElseThrow(()-> new IllegalStateException("'assets.index' setting is invalid."));

      ByteBuffer content = index.getContent();
      HtmlDocumentBuilder builder = new HtmlDocumentBuilder();
      try (InputStream is = new ByteBufferBackedInputStream(content)) {
        Document document = builder.parse(is);
        NodeList baseTags = document.getElementsByTagName("base");
        if (baseTags.getLength() > 0) {
          for (int i = 0, len = baseTags.getLength(); i < len; i++) {
            Node baseTag = baseTags.item(i);
            Node href = baseTag.getAttributes().getNamedItem("href");
            href.setNodeValue(BASE_HREF_PLACEHOLDER);
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

          indexHtml = os.toString();
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

    byte[] content = indexHtml.replace(BASE_HREF_PLACEHOLDER, getBaseHref(req)).getBytes();
    Asset asset = new Asset(ByteBuffer.wrap(content), index.getContentType(), content.length, index.getLastModified());
    sendAsset(res, asset);
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

  private String getBaseHref(HttpServletRequest req) {
    URI uri = URI.create(req.getRequestURL().toString());
    StringBuilder path = new StringBuilder();
    path.append(req.getContextPath());
    path.append(req.getServletPath());
    if (path.length() > 0) {
      path.append('/');
    }
    try {
      return new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), path.toString(), null, null).toString();
    } catch (URISyntaxException e) {
      throw new IllegalStateException(e);
    }
  }

}
