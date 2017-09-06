package jp.co.freemind.calico.csv;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.freemind.csv.Location;

public class CsvResult<T> {
  private final List<T> lines;
  private final Map<Location, String> errors;

  CsvResult(List<T> lines, Map<Location, String> errors, String formatError) {
    this.lines = Collections.unmodifiableList(lines);
    Map<Location, String> newErrors = new HashMap<>();
    errors.forEach((k, v) -> newErrors.put(k, v != null ? v : formatError));
    this.errors = Collections.unmodifiableMap(newErrors);
  }

  public boolean hasError() {
    return errors.size() > 0;
  }

  public List<T> getLines() {
    return lines;
  }

  public Map<Location, String> getErrors() {
    return errors;
  }

  public boolean isInvalid() {
    return errors.size() > 0;
  }
}
