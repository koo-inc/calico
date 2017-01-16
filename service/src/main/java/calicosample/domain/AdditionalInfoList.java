package calicosample.domain;

import java.util.ArrayList;
import java.util.List;

import calicosample.extenum.InfoSource;
import jp.co.freemind.calico.jackson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.Domain;

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
    private InfoSource infoSource;
    private boolean sensitiveInfo;
  }
}
