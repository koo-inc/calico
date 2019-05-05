package jp.co.freemind.calico.core.auth;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Strings;

import jp.co.freemind.calico.core.util.CryptoUtil;
import jp.co.freemind.calico.core.util.RandomUtil;
import jp.co.freemind.calico.core.zone.Zone;

public class AuthToken {
  @Nonnull private String value;
  @Nonnull private String id;
  @Nonnull private String remoteAddress;
  @Nonnull private String csrfToken;
  @Nonnull private LocalDateTime createdAt;

  private static final ZoneOffset ZONE_OFFSET = ZoneOffset.UTC;
  private static final long TIME_OFFSET = LocalDateTime.MIN.toEpochSecond(ZONE_OFFSET);

  private AuthToken(String id, String remoteAddress, String csrfToken, LocalDateTime createdAt) {
    long sec = createdAt.toEpochSecond(ZONE_OFFSET) - TIME_OFFSET;
    this.id = id;
    this.remoteAddress = remoteAddress.replaceAll("[^0-9:.%]", " ");
    this.csrfToken = csrfToken;
    this.createdAt = createdAt;
    this.value = CryptoUtil.encrypt(
      sec + "@" + csrfToken + "@" + remoteAddress + "@" + id,
      getSecretToken()
    );
  }

  @Nullable
  public static AuthToken of(@Nullable String value) throws IllegalArgumentException {
    try {
      String decrypted = CryptoUtil.decrypt(value, getSecretToken());
      String[] array = decrypted.split("@", 4);
      String sec = array[0];
      String csrfToken = array[1];
      String remoteAddress = array[2];
      String id = array[3];
      LocalDateTime createdAt = LocalDateTime.ofEpochSecond(Long.parseLong(sec) + TIME_OFFSET, 0, ZONE_OFFSET);
      return new AuthToken(id, remoteAddress, csrfToken, createdAt);
    }
    catch (Exception e) {
      return null;
    }
  }

  public static AuthToken create(String id, String remoteAddress, LocalDateTime createdAt) {
    String csrfToken = RandomUtil.randomAlphanumeric(16);
    return new AuthToken(id, remoteAddress, csrfToken, createdAt);
  }

  public static AuthToken createEmpty(String remoteAddress, LocalDateTime createdAt) {
    return create("", remoteAddress, createdAt);
  }

  @Deprecated
  public AuthToken regenerate() {
    return regenerate(LocalDateTime.now());
  }

  public AuthToken regenerate(LocalDateTime createdAt) {
    return new AuthToken(id, remoteAddress, csrfToken, createdAt);
  }

  public boolean isValidRemoteAddress(String remoteAddress) {
    return this.remoteAddress.equals(remoteAddress.replaceAll("[^0-9:.%]", " "));
  }

  public boolean isValidCsrfToken(String csrfToken) {
    if (Strings.isNullOrEmpty(csrfToken)) return false;
    return this.csrfToken.equals(csrfToken);
  }

  public boolean hasBeenCreatedFor(Duration duration) {
    return Duration.between(createdAt, LocalDateTime.now()).compareTo(duration) > 0;
  }

  private static String getSecretToken() {
    return Zone.getCurrent().getInstance(AuthSetting.class).getSecretToken();
  }

  @Nonnull
  public String getValue() {
    return this.value;
  }

  @Nonnull
  public String getId() {
    return this.id;
  }

  @Nonnull
  public String getRemoteAddress() {
    return this.remoteAddress;
  }

  @Nonnull
  public String getCsrfToken() {
    return this.csrfToken;
  }

  @Nonnull
  public LocalDateTime getCreatedAt() {
    return this.createdAt;
  }

  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof AuthToken)) return false;
    final AuthToken other = (AuthToken) o;
    final Object this$value = this.getValue();
    final Object other$value = other.getValue();
    if (this$value == null ? other$value != null : !this$value.equals(other$value)) return false;
    final Object this$id = this.getId();
    final Object other$id = other.getId();
    if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
    final Object this$remoteAddress = this.getRemoteAddress();
    final Object other$remoteAddress = other.getRemoteAddress();
    if (this$remoteAddress == null ? other$remoteAddress != null : !this$remoteAddress.equals(other$remoteAddress))
      return false;
    final Object this$csrfToken = this.getCsrfToken();
    final Object other$csrfToken = other.getCsrfToken();
    if (this$csrfToken == null ? other$csrfToken != null : !this$csrfToken.equals(other$csrfToken)) return false;
    final Object this$createdAt = this.getCreatedAt();
    final Object other$createdAt = other.getCreatedAt();
    if (this$createdAt == null ? other$createdAt != null : !this$createdAt.equals(other$createdAt)) return false;
    return true;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final Object $value = this.getValue();
    result = result * PRIME + ($value == null ? 43 : $value.hashCode());
    final Object $id = this.getId();
    result = result * PRIME + ($id == null ? 43 : $id.hashCode());
    final Object $remoteAddress = this.getRemoteAddress();
    result = result * PRIME + ($remoteAddress == null ? 43 : $remoteAddress.hashCode());
    final Object $csrfToken = this.getCsrfToken();
    result = result * PRIME + ($csrfToken == null ? 43 : $csrfToken.hashCode());
    final Object $createdAt = this.getCreatedAt();
    result = result * PRIME + ($createdAt == null ? 43 : $createdAt.hashCode());
    return result;
  }

  public String toString() {
    return "AuthToken(value=" + this.getValue() + ", id=" + this.getId() + ", remoteAddress=" + this.getRemoteAddress() + ", csrfToken=" + this.getCsrfToken() + ", createdAt=" + this.getCreatedAt() + ")";
  }
}
