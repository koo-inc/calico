package jp.co.freemind.calico.core.time;

import org.seasar.doma.ExternalDomain;
import org.seasar.doma.jdbc.domain.DomainConverter;

@ExternalDomain
public class TimePointConverter implements DomainConverter<TimePoint, String> {
  @Override
  public String fromDomainToValue(TimePoint timePoint) {
    return timePoint.toString();
  }

  @Override
  public TimePoint fromValueToDomain(String value) {
    return TimePoint.of(value);
  }
}
