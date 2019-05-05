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

public class Mail {
  private static final ObjectMapper mapper = new ObjectMapper();

  private Envelope envelope;
  private List<Media> attachments;

  @java.beans.ConstructorProperties({"envelope", "attachments"})
  public Mail(Envelope envelope, List<Media> attachments) {
    this.envelope = envelope;
    this.attachments = attachments;
  }

  public void visit(PostMan dispatcher) {
    dispatcher.accept(envelope, attachments);
  }

  public static Builder builder() {
    return new Builder();
  }

  private Envelope getEnvelope() {
    return this.envelope;
  }

  private List<Media> getAttachments() {
    return this.attachments;
  }

  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Mail)) return false;
    final Mail other = (Mail) o;
    final Object this$envelope = this.getEnvelope();
    final Object other$envelope = other.getEnvelope();
    if (this$envelope == null ? other$envelope != null : !this$envelope.equals(other$envelope)) return false;
    final Object this$attachments = this.getAttachments();
    final Object other$attachments = other.getAttachments();
    if (this$attachments == null ? other$attachments != null : !this$attachments.equals(other$attachments))
      return false;
    return true;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final Object $envelope = this.getEnvelope();
    result = result * PRIME + ($envelope == null ? 43 : $envelope.hashCode());
    final Object $attachments = this.getAttachments();
    result = result * PRIME + ($attachments == null ? 43 : $attachments.hashCode());
    return result;
  }

  public String toString() {
    return "Mail(envelope=" + this.getEnvelope() + ", attachments=" + this.getAttachments() + ")";
  }

  public static class Recipient {
    private String id;
    private String to;
    private Map<String, String> params;

    @java.beans.ConstructorProperties({"id", "to", "params"})
    public Recipient(String id, String to, Map<String, String> params) {
      this.id = id;
      this.to = to;
      this.params = params;
    }

    public String getId() {
      return this.id;
    }

    public String getTo() {
      return this.to;
    }

    public Map<String, String> getParams() {
      return this.params;
    }

    public boolean equals(Object o) {
      if (o == this) return true;
      if (!(o instanceof Recipient)) return false;
      final Recipient other = (Recipient) o;
      final Object this$id = this.getId();
      final Object other$id = other.getId();
      if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
      final Object this$to = this.getTo();
      final Object other$to = other.getTo();
      if (this$to == null ? other$to != null : !this$to.equals(other$to)) return false;
      final Object this$params = this.getParams();
      final Object other$params = other.getParams();
      if (this$params == null ? other$params != null : !this$params.equals(other$params)) return false;
      return true;
    }

    public int hashCode() {
      final int PRIME = 59;
      int result = 1;
      final Object $id = this.getId();
      result = result * PRIME + ($id == null ? 43 : $id.hashCode());
      final Object $to = this.getTo();
      result = result * PRIME + ($to == null ? 43 : $to.hashCode());
      final Object $params = this.getParams();
      result = result * PRIME + ($params == null ? 43 : $params.hashCode());
      return result;
    }

    public String toString() {
      return "Mail.Recipient(id=" + this.getId() + ", to=" + this.getTo() + ", params=" + this.getParams() + ")";
    }
  }

  public static class Envelope {
    private String id;
    private LocalDateTime reservedAt;
    private String fromAddress;
    private String fromName;
    private String subject;
    private String replyTo;
    private String body;
    private List<Recipient> recipients;

    @java.beans.ConstructorProperties({"id", "reservedAt", "fromAddress", "fromName", "subject", "replyTo", "body", "recipients"})
    public Envelope(String id, LocalDateTime reservedAt, String fromAddress, String fromName, String subject, String replyTo, String body, List<Recipient> recipients) {
      this.id = id;
      this.reservedAt = reservedAt;
      this.fromAddress = fromAddress;
      this.fromName = fromName;
      this.subject = subject;
      this.replyTo = replyTo;
      this.body = body;
      this.recipients = recipients;
    }

    public String getId() {
      return this.id;
    }

    public LocalDateTime getReservedAt() {
      return this.reservedAt;
    }

    public String getFromAddress() {
      return this.fromAddress;
    }

    public String getFromName() {
      return this.fromName;
    }

    public String getSubject() {
      return this.subject;
    }

    public String getReplyTo() {
      return this.replyTo;
    }

    public String getBody() {
      return this.body;
    }

    public List<Recipient> getRecipients() {
      return this.recipients;
    }

    public boolean equals(Object o) {
      if (o == this) return true;
      if (!(o instanceof Envelope)) return false;
      final Envelope other = (Envelope) o;
      final Object this$id = this.getId();
      final Object other$id = other.getId();
      if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
      final Object this$reservedAt = this.getReservedAt();
      final Object other$reservedAt = other.getReservedAt();
      if (this$reservedAt == null ? other$reservedAt != null : !this$reservedAt.equals(other$reservedAt))
        return false;
      final Object this$fromAddress = this.getFromAddress();
      final Object other$fromAddress = other.getFromAddress();
      if (this$fromAddress == null ? other$fromAddress != null : !this$fromAddress.equals(other$fromAddress))
        return false;
      final Object this$fromName = this.getFromName();
      final Object other$fromName = other.getFromName();
      if (this$fromName == null ? other$fromName != null : !this$fromName.equals(other$fromName)) return false;
      final Object this$subject = this.getSubject();
      final Object other$subject = other.getSubject();
      if (this$subject == null ? other$subject != null : !this$subject.equals(other$subject)) return false;
      final Object this$replyTo = this.getReplyTo();
      final Object other$replyTo = other.getReplyTo();
      if (this$replyTo == null ? other$replyTo != null : !this$replyTo.equals(other$replyTo)) return false;
      final Object this$body = this.getBody();
      final Object other$body = other.getBody();
      if (this$body == null ? other$body != null : !this$body.equals(other$body)) return false;
      final Object this$recipients = this.getRecipients();
      final Object other$recipients = other.getRecipients();
      if (this$recipients == null ? other$recipients != null : !this$recipients.equals(other$recipients))
        return false;
      return true;
    }

    public int hashCode() {
      final int PRIME = 59;
      int result = 1;
      final Object $id = this.getId();
      result = result * PRIME + ($id == null ? 43 : $id.hashCode());
      final Object $reservedAt = this.getReservedAt();
      result = result * PRIME + ($reservedAt == null ? 43 : $reservedAt.hashCode());
      final Object $fromAddress = this.getFromAddress();
      result = result * PRIME + ($fromAddress == null ? 43 : $fromAddress.hashCode());
      final Object $fromName = this.getFromName();
      result = result * PRIME + ($fromName == null ? 43 : $fromName.hashCode());
      final Object $subject = this.getSubject();
      result = result * PRIME + ($subject == null ? 43 : $subject.hashCode());
      final Object $replyTo = this.getReplyTo();
      result = result * PRIME + ($replyTo == null ? 43 : $replyTo.hashCode());
      final Object $body = this.getBody();
      result = result * PRIME + ($body == null ? 43 : $body.hashCode());
      final Object $recipients = this.getRecipients();
      result = result * PRIME + ($recipients == null ? 43 : $recipients.hashCode());
      return result;
    }

    public String toString() {
      return "Mail.Envelope(id=" + this.getId() + ", reservedAt=" + this.getReservedAt() + ", fromAddress=" + this.getFromAddress() + ", fromName=" + this.getFromName() + ", subject=" + this.getSubject() + ", replyTo=" + this.getReplyTo() + ", body=" + this.getBody() + ", recipients=" + this.getRecipients() + ")";
    }
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

    public <ID> Mail build(@Nonnull ID id) {
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
