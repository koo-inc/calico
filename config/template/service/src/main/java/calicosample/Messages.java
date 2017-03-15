package calicosample;

import java.util.Map;
import java.util.function.Function;

import jp.co.freemind.calico.core.endpoint.validation.Message;
import jp.co.freemind.calico.core.endpoint.validation.FieldAccessor;

/**
 * このクラスは自動生成されました。
 * 変更する場合はプロパティファイルのほうを修正してください。
 */
public final class Messages {
  <%message.each {msg ->
    if (msg.value.indexOf("{") > -1) {
      out << """
      public static Function<FieldAccessor, Message> ${msg.key.replaceAll(/([A-Z])/) { "_" + it[1] }.toUpperCase()}(Function<FieldAccessor, Map<String, Object>> mapper) {
        return field -> new Message("${msg.value}", mapper);
      }
      """
    }
    else {
      out << """
      public static final Message ${msg.key.replaceAll(/([A-Z])/) { "_" + it[1] }.toUpperCase() } = new Message("${msg.value}");
      """
    }
  }%>
}
