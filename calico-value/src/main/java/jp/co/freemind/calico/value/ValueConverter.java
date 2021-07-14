package jp.co.freemind.calico.value;

@FunctionalInterface
public interface ValueConverter <T> {
  T convert(String in) throws Exception;
}
