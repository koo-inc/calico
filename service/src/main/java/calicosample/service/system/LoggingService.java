package calicosample.service.system;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.Stream;

import javax.inject.Named;
import javax.sql.DataSource;

import calicosample.dao.log.LoggingDao;
import calicosample.entity.log.AccessEndLog;
import calicosample.entity.log.AccessStartLog;
import calicosample.entity.log.BatchEndLog;
import calicosample.entity.log.BatchStartLog;
import calicosample.entity.log.ErrorLog;
import calicosample.entity.log.JsLog;
import calicosample.entity.log.LogEntity;
import com.google.common.base.CaseFormat;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import jp.co.freemind.calico.core.auth.AuthInfo;
import jp.co.freemind.calico.core.auth.AuthToken;
import jp.co.freemind.calico.core.config.SystemSetting;
import jp.co.freemind.calico.core.zone.Context;
import jp.co.freemind.calico.core.zone.Zone;
import jp.co.freemind.calico.mail.Mail;
import jp.co.freemind.calico.mail.Mailer;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

/**
 * Created by kakusuke on 15/06/22.
 */
@Log4j2
@Singleton
public class LoggingService {
  @Inject private LoggingDao dao;
  @Inject @Named("log") private DataSource ds;
  @Inject private Mailer mailer;
  @Inject private SystemSetting systemSetting;
  @Inject private ErrorMailSetting errorMailSetting;

  @Getter @Setter
  public static class JsLogForm {
    private String location;
    private String userAgent;
    private String exception;
  }

  public int insert(JsLogForm form) {
    if (!isLoggable()) return 0;
    Context context = Zone.getContext();
    AuthInfo authInfo = context.getAuthInfo();
    AuthToken authToken = authInfo.getAuthToken();

    JsLog log = new JsLog();
    log.setLocation(form.getLocation());
    log.setUserAgent(form.getUserAgent());
    log.setException(form.getException());
    log.setLoginId(authInfo.getLoginId());
    log.setRemoteAddr(authToken.getRemoteAddress());
    log.setSessionId(authToken.getValue());
    log.setException(form.getException());
    return dao.insert(log);
  }

  public int insert(AccessStartLog log) {
    if (!isLoggable()) return 0;
    return dao.insert(log);
  }

  public int insert(JsLog log) {
    if (!isLoggable()) return 0;
    return dao.insert(log);
  }

  public int insert(ErrorLog log) {
    if(errorMailSetting.active()) mailer.getPostMan().deliver(buildErrorMail(log));
    if (!isLoggable()) return 0;
    return dao.insert(log);
  }

  public int insert(BatchStartLog log) {
    if (!isLoggable()) return 0;
    return dao.insert(log);
  }

  public int insert(BatchEndLog log) {
    if (!isLoggable()) return 0;
    return dao.insert(log);
  }

  public int insert(AccessEndLog log) {
    if (!isLoggable()) return 0;
    return dao.insert(log);
  }

  public <T extends LogEntity> void rotate(Class<T> logClass) {
    dao.rotate(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, logClass.getSimpleName()));
  }

  @Getter(lazy = true) private final boolean loggable = checkLoggable();
  private boolean checkLoggable() {
    boolean loggable;
    try (Connection conn = ds.getConnection()) {
      loggable = conn != null;
    }
    catch (SQLException e) {
      loggable = false;
    }

    if (!loggable) log.warn("ログDBにアクセスできないためログが保存できません");
    return loggable;
  }

  private Mail buildErrorMail(ErrorLog errorLog) {
     Mail.Builder builder = Mail.builder()
      .from(errorMailSetting.getSender(), errorMailSetting.getSenderName())
      .subject(String.format("%s(%s) Exception Occured!", systemSetting.getRootPackage(), systemSetting.getEnvName()))
      .body(errorLog.toString());

    Stream.of(errorMailSetting.getReceivers().split("\\s*,\\s*"))
      .forEach(builder::to);

    return builder.build(String.valueOf(errorLog.getStartLogId()));
  }

}
