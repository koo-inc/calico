package jp.co.freemind.calico.core.zone;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import jp.co.freemind.calico.core.auth.AuthInfo;

public class Context {
  private final Spec spec;
  private final String path;
  private final AuthInfo authInfo;
  private final LocalDateTime processDateTime;
  private final String remoteAddress;
  private final MetaInfo metaInfo;

  public Context(Function<Spec, Spec> initializer) {
    this(initializer.apply(new Spec()));
  }

  public Context(Context context) {
    this(context.spec);
  }

  private Context(Spec spec) {
    Objects.requireNonNull(spec.path);
    Objects.requireNonNull(spec.authInfo);
    Objects.requireNonNull(spec.processDateTime);
    Objects.requireNonNull(spec.remoteAddress);
    Objects.requireNonNull(spec.metaInfo);

    this.spec = spec;
    this.path = spec.path;
    this.authInfo = spec.authInfo;
    this.processDateTime = spec.processDateTime;
    this.remoteAddress = spec.remoteAddress;
    this.metaInfo = new MetaInfo(spec.metaInfo);
  }

  public String getPath() {
    return this.path;
  }

  @SuppressWarnings("unchecked")
  @Nonnull
  public <T extends AuthInfo> T getAuthInfo() {
    return (T) authInfo;
  }

  public LocalDateTime getProcessDateTime() {
    return processDateTime;
  }

  public LocalDate getProcessDate() {
    return getProcessDateTime().toLocalDate();
  }

  public String getRemoteAddress() {
    return remoteAddress;
  }

  public MetaInfo getMetaInfo() {
    return metaInfo;
  }

  @SuppressWarnings("unchecked")
  public Context extend(Function<Spec, Spec> initializer) {
    return new Context(
      initializer.apply(new Spec(path, authInfo, processDateTime, remoteAddress, metaInfo.entrySet())));
  }

  public static class Spec {
    private final String path;
    private final AuthInfo authInfo;
    private final LocalDateTime processDateTime;
    private final String remoteAddress;
    private final Set<Map.Entry<String, String>> metaInfo;

    @SuppressWarnings("unchecked")
    private Spec() {
      this(null, null, null, null, new LinkedHashSet<>());
    }
    private Spec(String path, AuthInfo authInfo, LocalDateTime processDateTime, String remoteAddress, @Nonnull Set<Map.Entry<String, String>> metaInfo) {
      this.path = path;
      this.authInfo = authInfo;
      this.processDateTime = processDateTime;
      this.remoteAddress = remoteAddress;
      this.metaInfo = metaInfo;
    }
    public Spec path(String path) {
      return new Spec(path, authInfo, processDateTime, remoteAddress, metaInfo);
    }
    public Spec authInfo(@Nonnull AuthInfo authInfo) {
      return new Spec(path, authInfo, processDateTime, remoteAddress, metaInfo);
    }
    public Spec processDateTime(@Nonnull LocalDateTime processDateTime) {
      return new Spec(path, authInfo, processDateTime, remoteAddress, metaInfo);
    }
    public Spec remoteAddress(@Nonnull String remoteAddress) {
      return new Spec(path, authInfo, processDateTime, remoteAddress, metaInfo);
    }
    public Spec entry(@Nonnull String key, @Nullable String value) {
      Set<Map.Entry<String, String>> set = new LinkedHashSet<>(metaInfo);
      set.add(new AbstractMap.SimpleEntry<>(key, value));
      return new Spec(path, authInfo, processDateTime, remoteAddress, set);
    }
  }
}
