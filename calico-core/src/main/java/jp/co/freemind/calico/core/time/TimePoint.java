package jp.co.freemind.calico.core.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.Strings;
import org.seasar.doma.Domain;

@Domain(valueType = String.class, factoryMethod = "of", accessorMethod = "toString")
public class TimePoint implements Comparable<TimePoint> {
  private final int minutesAmount;

  private TimePoint(int minutes) {
    this.minutesAmount = minutes;
  }

  public int getHours() {
    return minutesAmount / 60;
  }
  public int getMinutes() {
    return minutesAmount % 60;
  }
  public int getMinutesAmount() {
    return minutesAmount;
  }
  public int sub(@Nonnull TimePoint tp) {
    return this.minutesAmount - tp.minutesAmount;
  }

  public LocalTime toLocalTime() {
    return LocalTime.of(getHours() % 24, getMinutes());
  }
  public LocalDateTime toLocalDateTime(LocalDate baseDate) {
    return baseDate.atTime(LocalTime.MIN).plusMinutes(minutesAmount);
  }
  public OffsetDateTime toOffsetDateTime(LocalDate baseDate) {
    return toZonedDateTime(baseDate).toOffsetDateTime();
  }
  public OffsetDateTime toOffsetDateTime(LocalDate baseDate, ZoneOffset zoneOffset) {
    return baseDate.atTime(LocalTime.MIN).atOffset(zoneOffset).plusMinutes(minutesAmount);
  }
  public ZonedDateTime toZonedDateTime(LocalDate baseDate) {
    return toZonedDateTime(baseDate, ZoneId.systemDefault());
  }
  public ZonedDateTime toZonedDateTime(LocalDate baseDate, ZoneId zoneId) {
    return baseDate.atTime(LocalTime.MIN).atZone(zoneId).plusMinutes(minutesAmount);
  }

  @Override
  public int compareTo(@Nonnull TimePoint o) {
    return Objects.compare(this, o, Comparator.comparing(tp -> tp.minutesAmount));
  }

  @Override
  @JsonValue
  public String toString() {
    return Strings.padStart(String.valueOf(this.getHours()), 2, '0')
      + ":" + Strings.padStart(String.valueOf(this.getMinutes()), 2, '0');
  }

  private static final Pattern PATTERN = Pattern.compile("^([0-9]{1,2}):([0-9]{1,2})$");
  @Nullable
  @JsonCreator
  public static TimePoint of(CharSequence value) {
    Matcher matcher = PATTERN.matcher(value);
    if (matcher.matches()) {
      int hours = Integer.parseInt(matcher.group(1));
      int minutes = Integer.parseInt(matcher.group(2));
      return of(hours, minutes);
    }
    return null;
  }

  public static TimePoint of(int hours, int minutes) {
    if (hours < 0 || minutes < 0) {
      throw new IllegalArgumentException("hours or minutes must not be negative, but [" + hours + ":" + minutes + "]");
    }
    return new TimePoint(hours * 60 + minutes);
  }
}
