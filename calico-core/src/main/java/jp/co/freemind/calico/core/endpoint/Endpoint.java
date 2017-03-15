package jp.co.freemind.calico.core.endpoint;

public interface Endpoint<INPUT, OUTPUT> {
  OUTPUT execute(INPUT input);
}
