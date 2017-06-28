package calicosample.entity.log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by kakusuke on 15/06/16.
 */
public interface Log {
  void setId(Long id);
  Long getId();
  LocalDateTime getTs();
  void setKey(String key);
  String getKey();


  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuuMM");
  default void preInsert() {
    setKey(getTs().format(formatter));
  }
}
