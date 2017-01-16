package calicosample.core.fmstorage;

import jp.co.freemind.calico.config.env.Env;
import jp.co.freemind.calico.config.env.EnvPath;
import jp.co.freemind.calico.config.env.PartialEnv;

/**
 * Created by tasuku on 15/04/27.
 */
@EnvPath("fmstorage")
public interface FmStorageEnv extends PartialEnv {
  String getUrl();
  String getBasePath();
  default String getCharset() {
    return "UTF-8";
  }

  static FmStorageEnv getEnv() {
    return Holder.INSTANCE;
  }

  class Holder {
    private final static FmStorageEnv INSTANCE = loadEnv();

    private static FmStorageEnv loadEnv() {
      return Env.loadPartial(FmStorageEnv.class);
    }
  }
}
