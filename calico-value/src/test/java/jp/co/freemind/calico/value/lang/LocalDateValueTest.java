package jp.co.freemind.calico.value.lang;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class LocalDateValueTest {

  @ParameterizedTest
  @CsvSource({
    "2021-04-04T15:00:00,2021-04-04",
    "2021-04-04T15:00:00Z,2021-04-04",
    "2021-04-05,2021-04-05",
    "2021/04/05,2021-04-05"
  })
  void パースできること(String input, String expected) {
    LocalDateValue value = new LocalDateValue(input);
    assert value.getValue().equals(LocalDate.parse(expected, DateTimeFormatter.ISO_LOCAL_DATE));
  }
}
