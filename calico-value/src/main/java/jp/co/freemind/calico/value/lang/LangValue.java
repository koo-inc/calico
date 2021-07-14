package jp.co.freemind.calico.value.lang;

import jp.co.freemind.calico.core.endpoint.validation.Message;
import jp.co.freemind.calico.core.util.Throwables;
import jp.co.freemind.calico.core.validation.Validatable;
import jp.co.freemind.calico.value.Value;
import jp.co.freemind.calico.value.ValueConverter;

public abstract class LangValue<T> extends Value<T> implements Validatable {
  protected LangValue(Object rawValue) {
    super(rawValue);
  }

  protected abstract Class<T> getComponentClass();
  abstract protected ValueConverter<T> getConverter();

  @SuppressWarnings("unchecked")
  @Override
  public T getValue() {
    if(isAssignableRawValue()){
      return (T) rawValue;
    }
    try {
      return convertValue();
    } catch (Throwable t) {
      throw Throwables.sneakyThrow(t);
    }
  }

  @Override
  public boolean isValid() {
    try {
      ensureValid();
      return true;
    } catch (Exception ex) {
      return false;
    }
  }

  protected void ensureValid() {
    if (isAssignableRawValue()) {
      return;
    }
    try {
      convertValue();
    } catch (Exception e) {
      throw Throwables.sneakyThrow(e);
    }
  }

  @Override
  public Message getInvalidMessage() {
    return message("不正な形式です。");
  }

  protected boolean isAssignableRawValue(){
    return getComponentClass().isAssignableFrom(rawValue.getClass());
  }

  private T convertValue() throws Exception {
    return getConverter().convert(rawValue.toString());
  }
}
