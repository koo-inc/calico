package calicosample.api.resource;


import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import calicosample.core.api.ApiResource;
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
import calicosample.dto.record.customer.CustomerSearchResult;
import calicosample.service.CustomerService;

@Path("customer")
public class CustomerApiResource extends ApiResource {
  @Inject private CustomerService customerService;

  @Path("search_form")
  @POST
  public CustomerSearchForm searchForm(CustomerSearchForm form){
    return customerService.getSearchForm();
  }

  @Path("search")
  @POST
  public CustomerSearchResult search(CustomerSearchForm form){
    return customerService.search(form);
  }

  @Path("record")
  @POST
  public CustomerRecord record(CustomerIdForm form){
    return customerService.getRecord(form);
  }

  @Path("create_form")
  @POST
  public CustomerCreateForm createForm(){
    return customerService.getCreateForm();
  }

  @Path("update_form")
  @POST
  public CustomerUpdateForm updateForm(CustomerIdForm form){
    return customerService.getUpdateForm(form);
  }

  @Path("create")
  @POST
  public CustomerRecord create(CustomerCreateForm form) {
    return customerService.create(form);
  }

  @Path("update")
  @POST
  public CustomerRecord update(CustomerUpdateForm form) {
    return customerService.update(form);
  }

  @Path("delete")
  @POST
  public CustomerRecord delete(CustomerIdForm form) {
    return customerService.delete(form);
  }

  @Path("random_create")
  @POST
  public CustomerRecord randomCreate(){
    return customerService.randomCreate(200);
  }

  @Path("download")
  @POST
  public CustomerPhotoRecord download(CustomerPhotoDownloadForm form) {
    return customerService.getPhoto(form);
  }

  @Path("download_family_csv")
  @POST
  public CustomerFamilyCsvRecord download(CustomerFamilyDownloadForm form) {
    return customerService.downloadFamilyCsv(form);
  }

  @Path("upload_family_csv")
  @POST
  public List<CustomerGeneralForm.Family> uploadFamilyCsv(CustomerFamilyUploadForm form) {
    return customerService.uploadFamily(form);
  }
}
