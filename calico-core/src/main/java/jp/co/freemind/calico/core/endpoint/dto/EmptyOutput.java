package jp.co.freemind.calico.core.endpoint.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class EmptyOutput {
  private static EmptyOutput instance;

  private EmptyOutput() {
  }

  public static EmptyOutput getInstance() {
    if(instance == null){
      instance = new EmptyOutput();
    }
    return instance;
  }
}
