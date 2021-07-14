package jp.co.freemind.calico.value.lang;

import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.freemind.calico.core.endpoint.validation.Message;
import jp.co.freemind.calico.value.ValueConverter;

public abstract class NumberValue<T extends Number> extends LangValue<T> {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  protected final boolean outOfRange;

  protected NumberValue(Object rawValue) {
    super(rawValue);
    boolean outOfRange = false;
    try {
      ensureValid();
    } catch (Exception ex) {
      outOfRange = ex.getMessage().contains("out of range of");
    }
    this.outOfRange = outOfRange;
  }

  @Override
  protected ValueConverter<T> getConverter() {
    return value -> objectMapper.readValue(rawValue.toString(), getComponentClass());
  }

  @Override
  public Message getInvalidMessage() {
    if(outOfRange){
      return message("有効な範囲を超えた値です。");
    }
    return super.getInvalidMessage();
  }
}
