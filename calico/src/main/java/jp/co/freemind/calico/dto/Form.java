package jp.co.freemind.calico.dto;

import java.util.Comparator;
import java.util.Map;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Form extends DTO {
  @JsonIgnore
  @Inject
  private Validator validator;

  public Map<String, String> validate(){
    Map<String, String> violations = Maps.newTreeMap(Comparator.<String>naturalOrder());
    for (ConstraintViolation<Form> violation : validator.validate(this)) {
      violations.put(violation.getPropertyPath().toString(), violation.getMessage());
    }
    return violations;
  }
}
