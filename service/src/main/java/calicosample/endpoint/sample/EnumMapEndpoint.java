package calicosample.endpoint.sample;

import java.util.List;
import java.util.Map;

import calicosample.extenum.Sex;
import jp.co.freemind.calico.core.endpoint.Endpoint;
import lombok.Data;

public class EnumMapEndpoint implements Endpoint<EnumMapEndpoint.Input, EnumMapEndpoint.Input> {

  @Override
  public Input execute(Input input) {
    return input;
  }

  @Data
  public static class Input {
    private Map<Sex, List<Sex>> enums;
  }
}
