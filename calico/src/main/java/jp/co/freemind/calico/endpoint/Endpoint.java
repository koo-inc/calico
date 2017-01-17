package jp.co.freemind.calico.endpoint;

import lombok.Getter;

@Getter
public abstract class Endpoint<INPUT, OUTPUT> {
  public abstract OUTPUT execute(INPUT form);
}
