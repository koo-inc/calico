package calicosample.core.mail;

import java.util.function.Supplier;

import com.google.inject.Singleton;
import jp.co.freemind.calico.config.env.Env;
import jp.co.freemind.calico.config.env.EnvPath;
import jp.co.freemind.calico.config.env.PartialEnv;
import jp.co.freemind.calico.mail.FmPostMan;
import jp.co.freemind.calico.mail.PostMan;
import jp.co.freemind.calico.mail.PrintPostMan;
import jp.co.freemind.calico.mail.Mailer;
import lombok.extern.log4j.Log4j2;

/**
 * Created by kakusuke on 15/07/06.
 */
@Log4j2
@Singleton
public class FmMailer implements Mailer {

  private final Supplier<PostMan> dispatcherSupplier;

  public FmMailer() {
    FmMailerEnv env = Env.loadPartial(FmMailerEnv.class);

    String apiUrl = env.getApiUrl();
    if (apiUrl == null) {
      log.warn("fmmail.apiUrl が設定されていません。ログに内容を出力します。");
      dispatcherSupplier = PrintPostMan::new;
    }
    else {
      dispatcherSupplier = ()-> new FmPostMan(apiUrl);
    }
  }

  @Override
  public PostMan getPostMan() {
    return dispatcherSupplier.get();
  }

  @EnvPath("fmmailer")
  public interface FmMailerEnv extends PartialEnv {
    String getApiUrl();
  }
}
