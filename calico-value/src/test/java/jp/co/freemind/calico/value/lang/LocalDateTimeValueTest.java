package jp.co.freemind.calico.value.lang;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class LocalDateTimeValueTest {

  @ParameterizedTest
  @CsvSource({
    "2021-04-04T15:00:00,2021-04-04T15:00:00",
    "2021-04-04T15:00:00Z,2021-04-04T15:00:00",
    "2021-04-05,2021-04-05T00:00:00",
    "2021/04/05,2021-04-05T00:00:00"
  })
  void パースできること(String input, String expected) {
    LocalDateTimeValue value = new LocalDateTimeValue(input);
    assert value.getValue().equals(LocalDateTime.parse(expected, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
  }
}
