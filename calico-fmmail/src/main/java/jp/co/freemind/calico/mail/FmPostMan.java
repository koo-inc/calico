package jp.co.freemind.calico.mail;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import jp.co.freemind.calico.core.media.Media;
import jp.co.freemind.calico.core.util.StreamUtil;
import jp.co.freemind.calico.core.util.Throwables;
import lombok.SneakyThrows;
import org.apache.http.Consts;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class FmPostMan implements PostMan {
  private final static DateTimeFormatter SEND_AT_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

  private final FmMailSetting setting;
  private final ObjectMapper objectMapper;
  private List<MultipartEntityBuilder> builders = Lists.newArrayList();

  @Inject
  public FmPostMan(FmMailSetting setting, ObjectMapper objectMapper) {
    this.setting = setting;
    this.objectMapper = objectMapper;
  }

  @Override
  public void accept(Mail.Envelope envelope) {
    accept(envelope, Collections.emptyList());
  }

  @Override
  @SneakyThrows
  public void accept(Mail.Envelope envelope, List<Media> mediaList) {
    if (mediaList.size() > 5) {
      throw new IllegalStateException("attachments attached too mach");
    }

    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
    builder.setMode(HttpMultipartMode.RFC6532);
    builder.addTextBody("mailInfo", getMailInfo(envelope), ContentType.create("text/plain", Consts.UTF_8));
    StreamUtil.forEachWithIndex(mediaList.stream(), (m, i) ->
      builder.addBinaryBody("attached" + (i + 1), m.getPayload(), ContentType.APPLICATION_OCTET_STREAM, m.getMeta().getName()));
    builders.add(builder);
  }

  @Override
  public void deliver(Mail mail) {
    mail.visit(this);

    builders.forEach(builder -> {
      HttpPost post = new HttpPost(setting.getApiUrl());
      post.setEntity(builder.build());

      try (CloseableHttpClient client = HttpClients.createDefault(); CloseableHttpResponse res = client.execute(post)) {
        validateResponse(res);
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    });
    builders = Lists.newArrayList();
  }

  private String getMailInfo(Mail.Envelope envelope) {
    Map<String, Object> mailInfo = new HashMap<>();
    mailInfo.put("id", normalizeId(envelope.getId()));
    mailInfo.put("sendAt", getSendAt(envelope.getReservedAt()));
    mailInfo.put("fromAddress", envelope.getFromAddress());
    mailInfo.put("fromName", envelope.getFromName());
    mailInfo.put("subject", envelope.getSubject());
    mailInfo.put("replayTo", envelope.getReplyTo());
    mailInfo.put("body", envelope.getBody());
    mailInfo.put("recipients", envelope.getRecipients().stream()
      .map(this::getRecipient)
      .collect(Collectors.toList()));
    try {
      return objectMapper.writeValueAsString(mailInfo);
    } catch (JsonProcessingException e) {
      throw new UncheckedIOException(e);
    }
  }

  private Map<String, Object> getRecipient(Mail.Recipient recipient) {
    Map<String, Object> ret = new HashMap<>();
    ret.put("id", normalizeId(recipient.getId()));
    ret.put("to", recipient.getTo());
    ret.put("params", recipient.getParams());
    return ret;
  }

  private String normalizeId(String id) {
    if (id == null) return null;
    id = id.replaceAll("[^-_a-zA-Z0-9]", "-");
    if (id.length() > 20) {
      return id.substring(0, 20);
    }
    else {
      return id;
    }
  }

  private String getSendAt(LocalDateTime reservedAt) {
    if (reservedAt == null || reservedAt.compareTo(LocalDateTime.now()) <= 0) {
      return "at once";
    }
    return reservedAt.format(SEND_AT_FORMATTER);
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
