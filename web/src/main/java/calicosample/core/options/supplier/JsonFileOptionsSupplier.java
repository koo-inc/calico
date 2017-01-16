package calicosample.core.options.supplier;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import calicosample.core.options.OptionsForm;
import calicosample.core.options.OptionsRecord;
import calicosample.core.options.OptionsSupplier;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

@AllArgsConstructor
public class JsonFileOptionsSupplier implements OptionsSupplier {
  private String rootPath;

  @Override
  public boolean isKeyMatch(String key) {
    return true;
  }

  @Override
  public Optional<OptionsRecord> get(OptionsForm form) {
    Optional<File> file = getFile(form.getKey());
    if(!file.isPresent()) return Optional.empty();

    LocalDateTime modifiedAt = getModifiedAt(file.get());
    if(form.getLastModifiedAt().isPresent() && !form.getLastModifiedAt().get().isBefore(modifiedAt)){
      return Optional.of(OptionsRecord.notModified(form.getKey()));
    }

    List<Object> data = getData(file.get());
    return data == null ? Optional.empty() : Optional.of(OptionsRecord.modified(form.getKey(), data, modifiedAt));
  }

  private Optional<File> getFile(String key){
    String path = rootPath + "/" + key + ".json";
    File file = new File(path);
    return file.exists() ? Optional.of(file) : Optional.empty();
  }

  private LocalDateTime getModifiedAt(File file){
    return LocalDateTime.ofEpochSecond(file.lastModified() / 1000, 0, ZoneOffset.ofHours(9));
  }

  @SuppressWarnings("unchecked")
  private List<Object> getData(File file) {
    String content;
    try {
      content = Files.toString(file, Charsets.UTF_8);
    } catch (IOException e) {
      return null;
    }

    List<Object> ret;
    try {
      ret = new ObjectMapper().readValue(content, List.class);
    } catch (IOException e) {
      return null;
    }
    return ret;
  }
}
