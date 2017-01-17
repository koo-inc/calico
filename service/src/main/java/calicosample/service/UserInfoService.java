package calicosample.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.Valid;

import calicosample.dao.UserInfoDao;
import calicosample.dto.form.userinfo.UniqueLoginIdCheckForm;
import calicosample.dto.form.userinfo.UserInfoCreateForm;
import calicosample.dto.form.userinfo.UserInfoIdForm;
import calicosample.dto.form.userinfo.UserInfoUpdateForm;
import calicosample.dto.record.userinfo.UserInfoRecord;
import calicosample.entity.UserInfo;
import jp.co.freemind.calico.dto.DTOUtil;
import jp.co.freemind.calico.service.Service;
import org.apache.commons.lang3.StringUtils;

public class UserInfoService extends Service {
  @Inject private UserInfoDao userInfoDao;

  public List<UserInfoRecord> getRecords(){
    return userInfoDao.findAll().stream().map(UserInfoRecord::create).collect(Collectors.toList());
  }

  public UserInfoRecord getRecord(@Valid UserInfoIdForm form) {
    return UserInfoRecord.create(userInfoDao.findById(form.getId()));
  }

  public UserInfoCreateForm getCreateForm(){
    return new UserInfoCreateForm();
  }

  public UserInfoUpdateForm getUpdateForm(@Valid UserInfoIdForm form) {
    return UserInfoUpdateForm.create(userInfoDao.findById(form.getId()));
  }

  public UserInfoRecord create(@Valid UserInfoCreateForm form) {
    UserInfo userInfo = DTOUtil.copyProperties(new UserInfo(), form);
    userInfoDao.isnert(userInfo);
    return UserInfoRecord.create(userInfo);
  }

  public UserInfoRecord update(@Valid UserInfoUpdateForm form) {
    UserInfo userInfo = userInfoDao.findById(form.getId());
    DTOUtil.copyProperties(userInfo, form);
    userInfoDao.update(userInfo);
    return UserInfoRecord.create(userInfo);
  }

  public UserInfoRecord delete(@Valid UserInfoIdForm form) {
    UserInfo userInfo = userInfoDao.findById(form.getId());
    userInfoDao.delete(userInfo);
    return UserInfoRecord.create(userInfo);
  }

  public Boolean isUniqueLoginId(@Valid UniqueLoginIdCheckForm form) {
    if(StringUtils.isEmpty(form.getLoginId())) return true;
    return userInfoDao.findForLoginIdUniqueCheck(form.getLoginId(), form.getExceptId()).size() == 0;
  }
}
