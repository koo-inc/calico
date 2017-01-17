package jp.co.freemind.calico.endpoint.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
@JsonSerialize
public class EmptyOutput {
  private static EmptyOutput instance;

  public static EmptyOutput getInstance() {
    if(instance == null){
      instance = new EmptyOutput();
    }
    return instance;
  }
}
