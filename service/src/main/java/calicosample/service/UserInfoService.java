package calicosample.service;

import javax.inject.Inject;

import calicosample.dao.UserInfoDao;
import com.google.common.base.Strings;

public class UserInfoService {
  @Inject private UserInfoDao userInfoDao;

  public Boolean isUniqueLoginId(String loginId, Integer exceptId) {
    if(Strings.isNullOrEmpty(loginId)) return true;
    return userInfoDao.findForLoginIdUniqueCheck(loginId, exceptId).size() == 0;
  }
}
