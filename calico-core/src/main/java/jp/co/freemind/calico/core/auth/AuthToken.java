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
import lombok.NonNull;
import lombok.Value;

@Value public class AuthToken {
  @NonNull private String value;
  @NonNull private String id;
  @NonNull private String remoteAddress;
  @NonNull private String csrfToken;
  @NonNull private LocalDateTime createdAt;

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

  @Nonnull
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
      throw new IllegalArgumentException("Invalid auth token \"" + String.valueOf(value) + "\"", e);
    }
  }

  public static AuthToken create(String id, String remoteAddress, LocalDateTime createdAt) {
    String csrfToken = RandomUtil.randomAlphanumeric(16);
    return new AuthToken(id, remoteAddress, csrfToken, createdAt);
  }

  public static AuthToken createEmpty(String remoteAddress, LocalDateTime createdAt) {
    return create("", remoteAddress, createdAt);
  }

  public AuthToken regenerate() {
    return new AuthToken(id, remoteAddress, csrfToken, LocalDateTime.now());
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
}
