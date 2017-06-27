package jp.co.freemind.calico.servlet;

import java.util.Collections;

import javax.annotation.Nullable;

public class ServerErrorRenderer extends DefaultRenderer {
  public ServerErrorRenderer() {
    super(500);
  }

  @Override
  public void render(@Nullable Object obj) {
    super.render(Collections.singletonMap(
      "message", obj instanceof Throwable ? ((Throwable) obj).getMessage() : null)
    );
  }
}
