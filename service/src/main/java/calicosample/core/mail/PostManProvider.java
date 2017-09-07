package calicosample.core.mail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Provider;
import jp.co.freemind.calico.mail.FmMailSetting;
import jp.co.freemind.calico.mail.FmPostMan;
import jp.co.freemind.calico.mail.PostMan;
import jp.co.freemind.calico.mail.PrintPostMan;
import org.apache.logging.log4j.util.Strings;

public class PostManProvider implements Provider<PostMan> {
  @Inject private FmMailSetting setting;
  @Inject private ObjectMapper objectMapper;
  @Override
  public PostMan get() {
    if (Strings.isEmpty(setting.getApiUrl())) {
      return new PrintPostMan();
    }
    return new FmPostMan(setting, objectMapper);
  }
}
