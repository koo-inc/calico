package calicosample.core.options;

import java.time.LocalDateTime;
import java.util.Optional;

import jp.co.freemind.calico.dto.Record;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter(AccessLevel.PRIVATE)
public class OptionsRecord extends Record {
  private String key;
  private Optional<Object> data;
  private boolean cacheable;
  private Optional<Boolean> modified;
  private Optional<LocalDateTime> modifiedAt;

  private OptionsRecord(){}

  public static OptionsRecord uncacheable(String key, Object data){
    OptionsRecord ret = new OptionsRecord();
    ret.setKey(key);
    ret.setData(Optional.of(data));
    ret.setCacheable(false);
    ret.setModified(Optional.empty());
    ret.setModifiedAt(Optional.empty());
    return ret;
  }

  public static OptionsRecord cacheable(String key, Object data){
    OptionsRecord ret = new OptionsRecord();
    ret.setKey(key);
    ret.setData(Optional.of(data));
    ret.setCacheable(true);
    ret.setModified(Optional.empty());
    ret.setModifiedAt(Optional.empty());
    return ret;
  }

  public static OptionsRecord modified(String key, Object data, LocalDateTime modifiedAt){
    OptionsRecord ret = new OptionsRecord();
    ret.setKey(key);
    ret.setData(Optional.of(data));
    ret.setCacheable(true);
    ret.setModified(Optional.of(true));
    ret.setModifiedAt(Optional.of(modifiedAt));
    return ret;
  }

  public static OptionsRecord notModified(String key){
    OptionsRecord ret = new OptionsRecord();
    ret.setKey(key);
    ret.setData(Optional.empty());
    ret.setCacheable(false);
    ret.setModified(Optional.of(false));
    ret.setModifiedAt(Optional.empty());
    return ret;
  }
}
