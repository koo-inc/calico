package jp.co.freemind.calico.core.endpoint.exception;

public class UnknownEndpointException extends RuntimeException {
  public UnknownEndpointException(String path){
    super(String.format("cannot find endpoint of path: %s", path));
  }
}
