package calicosample.core;

import javax.annotation.Nullable;

public interface DbSettingBase {
  @Nullable
  String getJdbcUrl();
  @Nullable
  String getUser();
  @Nullable
  String getPassword();
}
