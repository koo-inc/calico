package jp.co.freemind.calico.servlet;

import java.util.Collections;

import javax.annotation.Nullable;

public class ServerErrorRenderer extends DefaultRenderer {
  public ServerErrorRenderer() {
    super(500);
  }

  @Override
  public void render(@Nullable Object output) {
    super.render(output != null ? output : Collections.emptyMap());
  }
}
