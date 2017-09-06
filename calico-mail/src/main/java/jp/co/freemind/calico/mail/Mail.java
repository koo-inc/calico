package jp.co.freemind.calico.mail;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import jp.co.freemind.calico.core.media.Media;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.Value;

@Value
@Getter(AccessLevel.PRIVATE)
public class Mail {
  private static final ObjectMapper mapper = new ObjectMapper();

  private Envelope envelope;
  private List<Media> attachments;

  @SneakyThrows
  public void visit(PostMan dispatcher) {
    dispatcher.accept(envelope, attachments);
  }

  public static Builder builder() {
    return new Builder();
  }

  @Value public static class Recipient {
    private String id;
    private String to;
    private Map<String, String> params;
  }

  @Value public static class Envelope {
    private String id;
    private LocalDateTime reservedAt;
    private String fromAddress;
    private String fromName;
    private String subject;
    private String replyTo;
    private String body;
    private List<Recipient> recipients;
  }

  public static class Builder {
    private String fromAddress;
    private String fromName;
    private String replyTo;
    private String subject;
    private String body;
    private LocalDateTime reservedAt;
    private ImmutableList.Builder<Recipient> recipients = ImmutableList.builder();
    private ImmutableList.Builder<Media> attachments = ImmutableList.builder();

    public Builder from(String address) {
      return from(address, address);
    }
    public Builder from(String address, String name) {
      this.fromAddress = address;
      this.fromName = name;
      return this;
    }

    public <ID> Builder to(@Nonnull ID id, String address) {
      return to(id, address, Collections.emptyMap());
    }
    public <ID> Builder to(@Nonnull ID id, String address, Map<String, String> params) {
      ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
      builder.putAll(params);
      return to(new Recipient(String.valueOf(id), address, builder.build()));
    }
    public Builder to(Recipient recipient) {
      this.recipients.add(recipient);
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

    public Builder reservedAt(LocalDateTime reservedAt) {
      this.reservedAt = reservedAt;
      return this;
    }

    public Builder attach(Media media) {
      this.attachments.add(media);
      return this;
    }

    public <ID> Mail build(@NonNull ID id) {
      fromName = fromName != null ? fromName : fromAddress;
      ensureNotNull("fromAddress", fromAddress);
      ensureNotNull("fromName", fromName);
      ensureNotNull("subject", subject);
      ensureNotNull("body", body);
      List<Recipient> immutableRecipients = recipients.build();
      if (immutableRecipients.size() == 0) {
        throw new IllegalStateException("recipients is empty.");
      }
      List<Media> immutableAttachments = attachments.build();

      return new Mail(new Envelope(String.valueOf(id), reservedAt, fromAddress, fromName, subject, replyTo, body, immutableRecipients), immutableAttachments);
    }

    private void ensureNotNull(String name, Object value) {
      if (value == null) {
        throw new IllegalStateException(name + " is null.");
      }
    }
  }
}
