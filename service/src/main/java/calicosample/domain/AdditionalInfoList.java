package calicosample.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.seasar.doma.Domain;
import calicosample.extenum.InfoSource;
import jp.co.freemind.calico.json.JsonObject;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by kakusuke on 15/08/07.
 */
@Domain(valueType = String.class)
public class AdditionalInfoList extends JsonObject<List<AdditionalInfoList.AdditionalInfo>> {
  public AdditionalInfoList() {
    super(new ArrayList<>());
  }
  public AdditionalInfoList(List<AdditionalInfoList.AdditionalInfo> object) {
    super(object);
  }
  public AdditionalInfoList(String json) {
    super(json);
  }

  @Getter @Setter
  public static class AdditionalInfo {
    private String name;
    private String content;
    private Optional<InfoSource> infoSource;
    private boolean sensitiveInfo;
  }
}
