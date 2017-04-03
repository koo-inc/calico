package calicosample.entity;

import java.time.LocalDate;

import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import calicosample.extenum.FamilyType;
import calicosample.extenum.Sex;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class CustomerFamily extends CalicoSampleEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private Customer.ID customerId;
  private FamilyType familyType;
  private String name;
  private Sex sex;
  private Integer favoriteNumber;
  private LocalDate birthday;
}
