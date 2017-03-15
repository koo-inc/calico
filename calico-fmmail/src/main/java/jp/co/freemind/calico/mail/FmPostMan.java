package jp.co.freemind.calico.mail;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import jp.co.freemind.calico.core.media.Media;
import jp.co.freemind.calico.core.util.StreamUtil;
import jp.co.freemind.calico.core.util.Throwables;
import org.apache.http.Consts;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Created by kakusuke on 15/07/06.
 */
public class FmPostMan implements PostMan {
  private final String apiUrl;
  private List<MultipartEntityBuilder> builders = Lists.newArrayList();

  public FmPostMan(String apiUrl) {
    this.apiUrl = apiUrl;
  }

  @Override
  public void accept(String envelopeJson) {
    accept(envelopeJson, Collections.emptyList());
  }

  public void accept(String envelopeJson, List<Media> mediaList) {
    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
    builder.setMode(HttpMultipartMode.RFC6532);
    builder.addTextBody("mailInfo", envelopeJson, ContentType.create("text/plain", Consts.UTF_8));
    StreamUtil.forEachWithIndex(mediaList.stream(), (m, i) ->
      builder.addBinaryBody("attached" + (i + 1), m.getPayload(), ContentType.APPLICATION_OCTET_STREAM, m.getMeta().getName()));
    builders.add(builder);
  }

  @Override
  public void deliver(Mail mail) {
    mail.visit(this);

    builders.forEach(builder -> {
      HttpPost post = new HttpPost(apiUrl);
      post.setEntity(builder.build());

      try (CloseableHttpClient client = HttpClients.createDefault(); CloseableHttpResponse res = client.execute(post)) {
        validateResponse(res);
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    });
    builders = Lists.newArrayList();
  }

  private void validateResponse(CloseableHttpResponse res) {
    String content;
    try {
      content = EntityUtils.toString(res.getEntity(), Charsets.UTF_8);
    } catch (IOException e) {
      throw Throwables.sneakyThrow(e);
    }
    if(!content.startsWith("success")) throw new MailException(content);
  }
}
