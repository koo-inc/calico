package calicosample.endpoint.customer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import calicosample.Messages;
import calicosample.core.validator.AllowEmpty;
import calicosample.core.validator.AllowedExtensions;
import calicosample.core.validator.FileSize;
import calicosample.core.validator.LowerBound;
import calicosample.core.validator.UpperBound;
import calicosample.domain.AdditionalInfoList;
import calicosample.entity.Customer;
import calicosample.entity.CustomerFamily;
import calicosample.extenum.FamilyType;
import calicosample.extenum.Sex;
import jp.co.freemind.calico.core.endpoint.Endpoint;
import jp.co.freemind.calico.core.endpoint.validation.Validate;
import jp.co.freemind.calico.core.media.Media;
import jp.co.freemind.calico.core.time.TimePoint;
import jp.co.freemind.calico.core.validation.Violation;
import jp.co.freemind.calico.core.zone.Zone;
import lombok.Getter;
import lombok.Setter;

public abstract class CustomerEndpoint<INPUT, OUTPUT> implements Endpoint<INPUT, OUTPUT> {

  @Getter @Setter
  public static class IdInput {
    private Customer.ID id;
  }

  @Getter @Setter
  public static class IdOutput {
    private Customer.ID id;
  }

  @Getter @Setter
  public static abstract class CommonFormInput {
    private String kname1;

    private String kname2;

    @Nullable
    private String fname1;

    @Nullable
    private String fname2;

    @Nullable
    private Sex sex;

    @LowerBound(1)
    @UpperBound(99)
    @Nullable
    private Integer favoriteNumber;

    private Boolean claimer;

    @Nullable
    private LocalDate birthday;

    @Nullable
    private LocalTime contactEnableStartTime;

    @Nullable
    private LocalTime contactEnableEndTime;

    @Nullable
    private String email;

    @Nullable
    private String homepageUrl;

    @Nullable
    private String phoneNumber;

    @Nullable
    private TimePoint sleepTime;

    @AllowedExtensions({"jpg", "png", "gif"})
    @FileSize(lowerBound = 1024 * 1024, upperBound = 1)
    @Nullable
    private Media photo;

    @Validate
    public void validateBirthday(Violation violation) {
      if (birthday == null) return;
      if (birthday.compareTo(Zone.getContext().getProcessDate()) >= 0) {
        violation.mark("birthday", Messages.PAST.value());
      }
    }

    @Validate
    protected void getContactEnableTime(Violation violation) {
      if (contactEnableStartTime == null || contactEnableEndTime == null) return;
      if (contactEnableStartTime.compareTo(contactEnableEndTime) >= 0) {
        violation.mark("contractEnableTime", Messages.FROM_TO_DATE.value());
      }
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
      setSleepTime(customer.getSleepTime());
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
      customer.setSleepTime(getSleepTime());
      customer.setPhoto(getPhoto());
      customer.setAdditionalInfoList(getAdditionalInfoList());
    }

    /**
     * Family
     */
    @AllowEmpty
    private List<Family> families = new ArrayList<>();

    private AdditionalInfoList additionalInfoList = new AdditionalInfoList();

    @Getter @Setter
    public static class Family {

      @Nullable
      private Integer id;

      @Nullable
      private FamilyType familyType;

      private String name;

      @Nullable
      private Sex sex;

      @LowerBound(1)
      @UpperBound(99)
      @Nullable
      private Integer favoriteNumber;

      @Nullable
      private LocalDate birthday;

      @Validate
      public void validateBirthday(Violation violation) {
        if (this.birthday == null) return;
        if (this.birthday.compareTo(Zone.getContext().getProcessDate()) >= 0) {
          violation.mark("birthday", Messages.PAST.value());
        }
      }

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
