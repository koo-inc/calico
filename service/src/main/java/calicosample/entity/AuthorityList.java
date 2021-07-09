package calicosample.entity;

import java.util.List;
import java.util.stream.Stream;

import org.seasar.doma.Domain;

import calicosample.extenum.CalicoSampleAuthority;
import jp.co.freemind.calico.jackson.JsonObject;

@Domain(valueType = String.class)
public class AuthorityList extends JsonObject<List<CalicoSampleAuthority>> /*implements JsonList<CalicoSampleAuthority> */ {
  public AuthorityList(String json) {
    super(json);
  }

  public Stream<CalicoSampleAuthority> stream() { return get().stream(); }
}
