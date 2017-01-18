package calicosample.endpoint.customer;

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

import calicosample.core.validator.AllowedExtensions;
import calicosample.core.validator.FileSize;
import calicosample.core.validator.FromTo;
import calicosample.core.validator.PhoneNumber;
import calicosample.domain.AdditionalInfoList;
import calicosample.entity.Customer;
import calicosample.entity.CustomerFamily;
import calicosample.extenum.FamilyType;
import calicosample.extenum.Sex;
import jp.co.freemind.calico.endpoint.Endpoint;
import jp.co.freemind.calico.media.Media;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

public abstract class CustomerEndpoint<INPUT, OUTPUT> extends Endpoint<INPUT, OUTPUT> {

  @Getter @Setter
  public static class IdInput {
    @NotNull
    private Integer id;
  }

  @Getter @Setter
  public static class IdOutput {
    private Integer id;
  }

  @Getter @Setter
  public static abstract class CommonFormInput {
    @NotEmpty
    private String kname1;

    @NotEmpty
    private String kname2;

    private String fname1;

    private String fname2;

    private Sex sex;

    @Min(1)
    @Max(99)
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

    @FromTo
    protected Pair<LocalTime, LocalTime> getContactEnableTime() {
      return ImmutablePair.of(getContactEnableStartTime(), getContactEnableEndTime());
    }

    public void copyFrom(Customer customer){
      setKname1(customer.getKname1());
      setKname2(customer.getKname2());
      setFname1(customer.getFname1());
      setFname2(customer.getFname2());
      setSex(customer.getSex());
      setFavoriteNumber(customer.getFavoriteNumber());
      setClaimer(customer.getClaimer());
      setBirthday(customer.getBirthday());
      setContactEnableStartTime(customer.getContactEnableStartTime());
      setContactEnableEndTime(customer.getContactEnableEndTime());
      setEmail(customer.getEmail());
      setHomepageUrl(customer.getHomepageUrl());
      setPhoneNumber(customer.getPhoneNumber());
      setPhoto(customer.getPhoto());
      setAdditionalInfoList(customer.getAdditionalInfoList());
    }

    public void copyTo(Customer customer){
      customer.setKname1(getKname1());
      customer.setKname2(getKname2());
      customer.setFname1(getFname1());
      customer.setFname2(getFname2());
      customer.setSex(getSex());
      customer.setFavoriteNumber(getFavoriteNumber());
      customer.setClaimer(getClaimer());
      customer.setBirthday(getBirthday());
      customer.setContactEnableStartTime(getContactEnableStartTime());
      customer.setContactEnableEndTime(getContactEnableEndTime());
      customer.setEmail(getEmail());
      customer.setHomepageUrl(getHomepageUrl());
      customer.setPhoneNumber(getPhoneNumber());
      customer.setPhoto(getPhoto());
      customer.setAdditionalInfoList(getAdditionalInfoList());
    }

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

      @Min(1)
      @Max(99)
      private Integer favoriteNumber;

      @Past
      private LocalDate birthday;

      @NotNull
      private Boolean delete = false;

      public static Family of(CustomerFamily customerFamily){
        return new Family(){{
          setId(customerFamily.getId());
          setFamilyType(customerFamily.getFamilyType());
          setName(customerFamily.getName());
          setSex(customerFamily.getSex());
          setFavoriteNumber(customerFamily.getFavoriteNumber());
          setBirthday(customerFamily.getBirthday());
        }};
      }

      public void copyTo(CustomerFamily customerFamily){
        customerFamily.setId(getId());
        customerFamily.setFamilyType(getFamilyType());
        customerFamily.setName(getName());
        customerFamily.setSex(getSex());
        customerFamily.setFavoriteNumber(getFavoriteNumber());
        customerFamily.setBirthday(getBirthday());
      }
    }
  }
}
