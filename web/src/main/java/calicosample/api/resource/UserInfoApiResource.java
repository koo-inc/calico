package calicosample.api.resource;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import calicosample.core.api.ApiResource;
import calicosample.dto.form.userinfo.UniqueLoginIdCheckForm;
import calicosample.dto.form.userinfo.UserInfoCreateForm;
import calicosample.dto.form.userinfo.UserInfoIdForm;
import calicosample.dto.form.userinfo.UserInfoUpdateForm;
import calicosample.dto.record.userinfo.UserInfoRecord;
import calicosample.service.UserInfoService;

@Path("user_info")
public class UserInfoApiResource extends ApiResource {
  @Inject private UserInfoService userInfoService;

  @Path("records")
  @POST
  public List<UserInfoRecord> records(){
    return userInfoService.getRecords();
  }

  @Path("record")
  @POST
  public UserInfoRecord record(UserInfoIdForm form){
    return userInfoService.getRecord(form);
  }

  @Path("create_form")
  @POST
  public UserInfoCreateForm createForm(){
    return userInfoService.getCreateForm();
  }

  @Path("update_form")
  @POST
  public UserInfoUpdateForm updateForm(UserInfoIdForm form){
    return userInfoService.getUpdateForm(form);
  }

  @Path("create")
  @POST
  public UserInfoRecord create(UserInfoCreateForm form) {
    return userInfoService.create(form);
  }

  @Path("update")
  @POST
  public UserInfoRecord update(UserInfoUpdateForm form) {
    return userInfoService.update(form);
  }

  @Path("delete")
  @POST
  public UserInfoRecord delete(UserInfoIdForm form) {
    return userInfoService.delete(form);
  }

  @Path("is_unique_login_id")
  @POST
  public Boolean isUniqeuLoginId(UniqueLoginIdCheckForm form) {
    return userInfoService.isUniqueLoginId(form);
  }

}
