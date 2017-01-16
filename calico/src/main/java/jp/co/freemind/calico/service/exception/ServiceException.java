package jp.co.freemind.calico.service.exception;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;

import lombok.Getter;

public class ServiceException extends RuntimeException {
  @Getter
  private LinkedHashMap<String, List<String>> violations = new LinkedHashMap<>();

  public ServiceException(){
  }

  public <T> ServiceException(Set<ConstraintViolation<T>> violations){
    addAll(violations);
  }

  public ServiceException(String key, String message){
    add(key, message);
  }

  public void add(String key, String message){
    if(!violations.containsKey(key)){
      violations.put(key, new ArrayList<>());
    }
    violations.get(key).add(message);
  }

  public <T> void add(ConstraintViolation<T> violation){
    add(violation.getPropertyPath().toString(), violation.getMessage());
  }

  public void addAll(Map<String, String> map){
    map.forEach(this::add);
  }

  public <T> void addAll(Set<ConstraintViolation<T>> violations){
    violations.forEach(this::add);
  }

}
