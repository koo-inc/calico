package jp.co.freemind.calico.servlet.renderer;

@FunctionalInterface
public interface Renderer<T> {
  void render(T output);
}
