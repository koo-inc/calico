package jp.co.freemind.calico.core.exception;

public class UnknownEndpointException extends ViolationException {
  public UnknownEndpointException(String path){
    super("unknownPath", path);
  }
}
