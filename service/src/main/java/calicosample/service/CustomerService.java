package calicosample.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.validation.Valid;

import calicosample.core.fmstorage.MediaIdGenerator;
import calicosample.dao.CustomerDao;
import calicosample.dto.form.customer.CustomerCreateForm;
import calicosample.dto.form.customer.CustomerFamilyDownloadForm;
import calicosample.dto.form.customer.CustomerFamilyUploadForm;
import calicosample.dto.form.customer.CustomerGeneralForm;
import calicosample.dto.form.customer.CustomerIdForm;
import calicosample.dto.form.customer.CustomerPhotoDownloadForm;
import calicosample.dto.form.customer.CustomerSearchForm;
import calicosample.dto.form.customer.CustomerUpdateForm;
import calicosample.dto.record.customer.CustomerFamilyCsvRecord;
import calicosample.dto.record.customer.CustomerPhotoRecord;
import calicosample.dto.record.customer.CustomerRecord;
import calicosample.dto.record.customer.CustomerRecord.CustomerFamilyRecord;
import calicosample.dto.record.customer.CustomerSearchResult;
import calicosample.entity.Customer;
import calicosample.entity.CustomerFamily;
import calicosample.util.CsvUtil;
import jp.co.freemind.calico.dto.DTOUtil;
import jp.co.freemind.calico.media.Media;
import jp.co.freemind.calico.media.MediaStorage;
import jp.co.freemind.calico.service.Service;
import jp.co.freemind.calico.service.exception.ServiceException;
import jp.co.freemind.csv.CsvMapper;
import jp.co.freemind.csv.CsvReader;
import jp.co.freemind.csv.CsvWriter;
import jp.co.freemind.csv.Location;
import lombok.SneakyThrows;
import org.seasar.doma.jdbc.SelectOptions;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;
import static jp.co.freemind.calico.dto.DTOUtil.copyProperties;
import static jp.co.freemind.calico.dto.DTOUtil.toInstanceOf;
import static jp.co.freemind.calico.util.OptionalUtil.boxing;

public class CustomerService extends Service {
  @Inject private CustomerDao customerDao;
  @Inject private MediaStorage mediaStorage;

  private final CsvMapper<CustomerFamilyRecord> csvMapper = CsvUtil.createMapperForDownload(CustomerFamilyRecord.class, CustomerFamilyRecord.CsvFormat.class);

  public CustomerSearchResult search(@Valid CustomerSearchForm form){
    SelectOptions options = form.getSelectOptions();
    List<CustomerSearchResult.Record> records = customerDao.search(form, options, s ->
        s.map(CustomerSearchResult.Record::of).collect(toList())
    );
    return CustomerSearchResult.of(options.getCount(), records);
  }

  public CustomerSearchForm getSearchForm(){
    return new CustomerSearchForm();
  }

  public CustomerRecord getRecord(@Valid CustomerIdForm form) {
    Customer customer = customerDao.findById(form.getId());
    return CustomerRecord.create(customer, customerDao.findFamiliesByCustomer(customer));
  }

  public CustomerCreateForm getCreateForm(){
    CustomerCreateForm form = new CustomerCreateForm();
    form.setClaimer(false);
    return form;
  }

  public CustomerUpdateForm getUpdateForm(@Valid CustomerIdForm form) {
    Customer customer = customerDao.findById(form.getId());
    return CustomerUpdateForm.create(customer, customerDao.findFamiliesByCustomer(customer));
  }

  public CustomerRecord create(@Valid CustomerCreateForm form) {
    Customer customer = copyProperties(new Customer(), form);
    form.getPhoto().ifPresent(photo -> {
      Media proxy = mediaStorage.store(photo, MediaIdGenerator::customerPhoto);
      customer.setPhoto(Optional.of(proxy));
    });
    customerDao.insert(customer);

    updateCustomerFamilies(customer, form.getFamilies());

    return CustomerRecord.create(customer, customerDao.findFamiliesByCustomer(customer));
  }

  public CustomerRecord update(@Valid CustomerUpdateForm form) {
    Customer customer = customerDao.findById(form.getId());
    copyProperties(customer, form);

    Optional<Media> oldPhoto = customer.getOriginalStates().getPhoto();
    form.getPhoto().ifPresent(photo -> {
      if(photo.getId() == null){
        Media proxy = mediaStorage.store(photo, MediaIdGenerator::customerPhoto);
        customer.setPhoto(Optional.of(proxy));
      }
    });

    customerDao.update(customer);
    updateCustomerFamilies(customer, form.getFamilies());

    oldPhoto.ifPresent(op -> {
      if (!customer.getPhoto().isPresent()) {
        mediaStorage.remove(op);
      } else {
        if (!op.getId().equals(customer.getPhoto().get().getId())) {
          mediaStorage.remove(op);
        }
      }
    });

    return CustomerRecord.create(customer, customerDao.findFamiliesByCustomer(customer));
  }

  public CustomerRecord delete(@Valid CustomerIdForm form) {
    Customer customer = customerDao.findById(form.getId());
    List<CustomerFamily> families = customerDao.findFamiliesByCustomer(customer);
    families.forEach(f -> customerDao.deleteFamily(f));
    customerDao.delete(customer);
    customer.getPhoto().ifPresent(mediaStorage::remove);
    return CustomerRecord.create(customer, Collections.emptyList());
  }

  /**
   * テスト用
   */
  public CustomerRecord randomCreate(int count){
    for(int i = 0;i < count;i++){
      Customer customer = new Customer();
      customer.setKname1("ランダム");
      customer.setKname2("太郎" + i);
      customer.setFname1("らんだむ");
      customer.setFname2("たろう" + i);
      customer.setClaimer(false);
      customerDao.insert(customer);
    }
    return new CustomerRecord();
  }

  public CustomerPhotoRecord getPhoto(CustomerPhotoDownloadForm form) {
    CustomerPhotoRecord record = new CustomerPhotoRecord();
    Optional<Media> media = mediaStorage.get(form.getId());
    if (!media.isPresent()) {
      throw new ServiceException("id", "存在しないファイルです");
    }
    media.ifPresent(record::setPhoto);
    return record;
  }

  @SneakyThrows
  public CustomerFamilyCsvRecord downloadFamilyCsv(@Valid CustomerFamilyDownloadForm form) {
    Customer customer = customerDao.findById(form.getId());

    try(ByteArrayOutputStream os = new ByteArrayOutputStream()) {
      CsvWriter<CustomerFamilyRecord> writer = csvMapper.createWriter();
      customerDao.findFamiliesByCustomer(customer, stream-> {
          stream.map(toInstanceOf(CustomerFamilyRecord::new)).forEach(writer.writeTo(os));
          return null;
      });

      return new CustomerFamilyCsvRecord(Media.create(os.toByteArray(), "顧客家族情報.csv", "text/csv"));
    }
  }

  @SneakyThrows
  public List<CustomerGeneralForm.Family> uploadFamily(@Valid CustomerFamilyUploadForm form) {
    CsvReader<CustomerFamilyRecord> reader = csvMapper.createReader();
    try (Stream<CustomerFamilyRecord> stream = reader.read(new ByteArrayInputStream(form.getCsv().getPayload()))) {
      List<CustomerFamilyRecord> records = stream.collect(toList());
      if (reader.getErrorLocations().size() == 0) {
        List<CustomerGeneralForm.Family> families = records.stream()
          .map(r -> DTOUtil.copyProperties(new CustomerGeneralForm.Family(), r)).collect(toList());
        families.forEach(f -> f.setId(null));
        return families;
      }
    }

    ServiceException exception = new ServiceException();

    reader.getErrorLocations().stream()
      .sorted(comparingInt(Location::getLineNumber).thenComparingInt(l -> l.getColumnNumber().orElse(-1)))
      .map(l -> boxing(l.getColumnNumber())
        .map(i -> String.format("%d 行 %d 列目 の項目の形式が不正です", l.getLineNumber(), i))
        .orElseGet(() -> String.format("%d 行目 の形式が不正です", l.getLineNumber())))
      .forEach(message -> exception.add("csv", message));

    throw exception;
  }

  private void updateCustomerFamilies(Customer customer, List<CustomerGeneralForm.Family> familyForms) {
    familyForms.stream().forEach(f -> {
      CustomerFamily family = copyProperties(new CustomerFamily(), f);
      if (f.getDelete()) {
        if (family.getId() != null) {
          customerDao.deleteFamily(family);
        }
      } else if (f.getId() != null) {
        customerDao.updateFamily(family);
      } else if (f.getId() == null) {
        customerDao.insertFamily(customer, family);
      }
    });
  }
}
