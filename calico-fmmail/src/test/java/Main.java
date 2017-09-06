import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collections;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.freemind.calico.core.media.Media;
import jp.co.freemind.calico.core.media.MediaMeta;
import jp.co.freemind.calico.mail.FmMailSetting;
import jp.co.freemind.calico.mail.FmPostMan;
import jp.co.freemind.calico.mail.Mail;
import jp.co.freemind.calico.mail.PrintPostMan;

public class Main {
  public static void main(String[] args) {

    Mail mail = Mail.builder()
      .from("tasuku@freemind.co.jp")
      .to(1, "tasuku@freemind.co.jp", Collections.singletonMap("name", "名前1"))
      .to(2, "tasuku@freemind.co.jp", Collections.singletonMap("name", "名前2"))
      .subject("テスト")
      .body("[name]さん\nテストです")
      .attach(getMediaMock(1))
      .build(1);

    FmPostMan postman = new FmPostMan(new FmMailSetting() {
      @Override
      public String getApiUrl() {
        return args[0];
      }
    }, new ObjectMapper());

    postman.deliver(mail);

    new PrintPostMan().deliver(mail);
  }

  static Media getMediaMock(int i) {
    try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
      os.write(("テスト" + i).getBytes());
      MediaMeta meta = new MediaMeta();
      meta.setName("テスト" + i + ".txt");
      meta.setSize((long) os.size());
      meta.setType("text/plain");
      Media media = new Media();
      media.setMeta(meta);
      media.setPayload(os.toByteArray());
      return media;
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
