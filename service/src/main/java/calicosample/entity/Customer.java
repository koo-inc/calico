package calicosample.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import calicosample.domain.AdditionalInfoList;
import calicosample.extenum.Sex;
import jp.co.freemind.calico.core.media.Media;
import jp.co.freemind.calico.core.orm.SerialIdentifier;
import jp.co.freemind.calico.core.time.TimePoint;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.Domain;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.OriginalStates;

@Entity
@Getter @Setter
public class Customer extends CalicoSampleEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private ID id;
  private String kname1;
  private String kname2;
  private String fname1;
  private String fname2;
  private Sex sex;
  private Integer favoriteNumber;
  private Boolean claimer;
  private LocalDate birthday;
  private LocalTime contactEnableStartTime;
  private LocalTime contactEnableEndTime;
  private String email;
  private String homepageUrl;
  private String phoneNumber;
  private TimePoint sleepTime;
  private Media photo;
  private AdditionalInfoList additionalInfoList;

  @OriginalStates
  private Customer originalStates;


  @Domain(valueType = Integer.class)
  public static class ID extends SerialIdentifier {
    public ID(Integer value) {
      super(value);
    }
  }
}
