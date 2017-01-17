package jp.co.freemind.calico.endpoint.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
@JsonSerialize
public class EmptyInput {
  private static EmptyInput instance;

  public static EmptyInput getInstance() {
    if(instance == null){
      instance = new EmptyInput();
    }
    return instance;
  }
}
