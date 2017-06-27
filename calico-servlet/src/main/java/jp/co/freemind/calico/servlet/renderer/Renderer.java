package jp.co.freemind.calico.servlet;

@FunctionalInterface
public interface Renderer<T> {
  void render(T output);
}
