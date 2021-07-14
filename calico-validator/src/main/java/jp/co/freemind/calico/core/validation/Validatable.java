package jp.co.freemind.calico.core.validation;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.common.collect.ImmutableMap;

import jp.co.freemind.calico.core.endpoint.validation.FieldAccessor;
import jp.co.freemind.calico.core.endpoint.validation.Message;

public interface Validatable {
  boolean isValid();
  Message getInvalidMessage();

  /**
   * Message作成補助
   */
  default Message message(String message){
    return new Message(message);
  }
  default Message message(
    Function<Function<FieldAccessor, Map<String, Object>>, Message> messageSupplier,
    Consumer<ImmutableMap.Builder<String, Object>> mapInitializer
  ){
    ImmutableMap.Builder<String, Object> mapBuilder = ImmutableMap.builder();
    mapInitializer.accept(mapBuilder);
    return messageSupplier.apply(f -> mapBuilder.build());
  }
}
