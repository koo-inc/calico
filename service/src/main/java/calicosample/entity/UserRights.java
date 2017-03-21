package calicosample.entity;

import calicosample.extenum.CalicoSampleRights;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;

@Entity
@Getter @Setter
public class UserRights extends CalicoSampleEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private Integer userId;
  private CalicoSampleRights rights;
}
