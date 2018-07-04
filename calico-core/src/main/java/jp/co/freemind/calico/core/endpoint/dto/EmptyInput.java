package jp.co.freemind.calico.core.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class EmptyInput {
  private static EmptyInput instance;

  private EmptyInput() {
  }

  @JsonCreator
  public static EmptyInput getInstance() {
    if(instance == null){
      instance = new EmptyInput();
    }
    return instance;
  }
}
