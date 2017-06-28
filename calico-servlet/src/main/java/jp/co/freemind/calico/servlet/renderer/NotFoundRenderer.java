package jp.co.freemind.calico.servlet.renderer;

import java.util.Collections;

import javax.annotation.Nullable;

public class NotFoundRenderer extends DefaultRenderer {
  public NotFoundRenderer() {
    super(404);
  }

  @Override
  public void render(@Nullable Object obj) {
    super.render(Collections.singletonMap(
      "message", obj instanceof Throwable ? ((Throwable) obj).getMessage() : null)
    );
  }
}
