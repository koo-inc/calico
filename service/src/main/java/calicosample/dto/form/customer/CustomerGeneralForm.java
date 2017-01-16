package calicosample.dto.form.customer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;
import calicosample.core.validator.AllowedExtensions;
import calicosample.core.validator.FileSize;
import calicosample.core.validator.FromTo;
import calicosample.core.validator.PhoneNumber;
import calicosample.domain.AdditionalInfoList;
import calicosample.extenum.FamilyType;
import calicosample.extenum.Sex;
import jp.co.freemind.calico.dto.Form;
import jp.co.freemind.calico.media.Media;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class CustomerGeneralForm extends Form {

  @NotEmpty
  private String kname1;

  @NotEmpty
  private String kname2;

  private String fname1;

  private String fname2;

  private Sex sex;

  @Min(1) @Max(99)
  private Integer favoriteNumber;

  @NotNull
  private Boolean claimer;

  @Past
  private LocalDate birthday;

  private LocalTime contactEnableStartTime;

  private LocalTime contactEnableEndTime;

  @Email
  private Optional<String> email;

  @URL
  private String homepageUrl;

  @PhoneNumber
  private String phoneNumber;

  @AllowedExtensions({"jpg", "png", "gif"})
  @FileSize(max = 1024 * 1024, min = 1)
  private Optional<Media> photo;

  /**
   * Family
   */
  @Valid
  private List<Family> families = new ArrayList<>();

  @NotNull
  private AdditionalInfoList additionalInfoList = new AdditionalInfoList();

  @Getter @Setter
  public static class Family {

    private Integer id;

    private FamilyType familyType;

    @NotNull
    private String name;

    private Sex sex;

    @Min(1) @Max(99)
    private Integer favoriteNumber;

    @Past
    private LocalDate birthday;

    @NotNull
    private Boolean delete = false;
  }

  /**
   * contactEnableTime from-toチェック
   */
  @FromTo
  protected Pair<LocalTime, LocalTime> getContactEnableTime(){
    return ImmutablePair.of(getContactEnableStartTime(), getContactEnableEndTime());
  }

}
