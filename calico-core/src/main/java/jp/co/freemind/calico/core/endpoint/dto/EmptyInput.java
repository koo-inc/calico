package jp.co.freemind.calico.core.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class EmptyInput {
  private static EmptyInput instance;

  @JsonCreator
  public static EmptyInput getInstance() {
    if(instance == null){
      instance = new EmptyInput();
    }
    return instance;
  }
}
