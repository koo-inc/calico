package jp.co.freemind.calico.mail;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import jp.co.freemind.calico.core.media.Media;
import jp.co.freemind.calico.core.util.RandomUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.Value;

/**
 * Created by kakusuke on 15/07/06.
 */
@Value
@Getter(AccessLevel.PRIVATE)
public class Mail {
  private static final ObjectMapper mapper = new ObjectMapper();

  private MailInfo mailInfo;
  private List<Media> attachments;

  @SneakyThrows
  public void visit(PostMan dispatcher) {
    dispatcher.accept(mapper.writeValueAsString(mailInfo), attachments);
  }

  public static Builder builder() {
    return new Builder();
  }

  @Value private static class Recipient {
    private String id;
    private String to;
    private Map<String, String> params;
  }

  @Value private static class MailInfo {
    private String id;
    private String sendAt;
    private String fromAddress;
    private String fromName;
    private String subject;
    private String replyTo;
    private String body;
    private List<Recipient> recipients;
  }

  public static class Builder {
    private final static DateTimeFormatter SEND_AT_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    private String fromAddress;
    private String fromName;
    private String replyTo;
    private String subject;
    private String body;
    private LocalDateTime reservedAt;
    private ImmutableList.Builder<Recipient> recipients = ImmutableList.builder();
    private ImmutableList.Builder<Media> attachements = ImmutableList.builder();

    public Builder from(String address) {
      return from(address, address);
    }
    public Builder from(String address, String name) {
      this.fromAddress = address;
      this.fromName = name;
      return this;
    }

    public Builder to(String address) {
      return to(RandomUtil.randomAlphanumeric(20), address, Collections.emptyMap());
    }
    public Builder to(String address, Map<String, String> params) {
      return to(RandomUtil.randomAlphanumeric(20), address, params);
    }
    public Builder to(String id, String address) {
      return to(id, address, Collections.emptyMap());
    }
    public Builder to(String id, String address, Map<String, String> params) {
      ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
      builder.putAll(params);
      this.recipients.add(new Recipient(normalizeId(id), address, builder.build()));
      return this;
    }

    public Builder replyTo(String address) {
      this.replyTo = address;
      return this;
    }

    public Builder subject(String subject) {
      this.subject = subject;
      return this;
    }

    public Builder body(String body) {
      this.body = body;
      return this;
    }

    public Builder reserveAt(LocalDateTime reservedAt) {
      this.reservedAt = reservedAt;
      return this;
    }

    public Builder attach(Media media) {
      this.attachements.add(media);
      return this;
    }

    public Mail build() {
      return build(RandomUtil.randomAlphanumeric(20));
    }
    public Mail build(@NonNull String id) {
      id = normalizeId(id);
      String sendAt = reservedAt != null ? reservedAt.format(SEND_AT_FORMATTER) : "at once";
      fromName = fromName != null ? fromName : fromAddress;
      ensureNotNull("fromAddress", fromAddress);
      ensureNotNull("fromName", fromName);
      ensureNotNull("subject", subject);
      ensureNotNull("body", body);
      List<Recipient> immutableRecipients = recipients.build();
      if (immutableRecipients.size() == 0) {
        throw new IllegalStateException("recipients is empty.");
      }
      List<Media> immutableAtachements = attachements.build();
      if (immutableAtachements.size() > 5) {
        throw new IllegalStateException("attachements atached too mach");
      }

      MailInfo mailInfo = new MailInfo(id, sendAt, fromAddress, fromName, subject, replyTo, body, immutableRecipients);
      return new Mail(mailInfo, immutableAtachements);
    }


    private String normalizeId(String id) {
      if (id == null) return null;
      id = id.replaceAll("[^a-zA-Z0-9-_]", "");
      if (id.length() > 20) {
        return id.substring(0, 20);
      }
      else {
        return id;
      }
    }

    private void ensureNotNull(String name, Object value) {
      if (value == null) {
        throw new IllegalStateException(name + " is null.");
      }
    }
  }
}
