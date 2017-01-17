package calicosample.service;

import javax.inject.Inject;

import calicosample.dao.UserInfoDao;
import jp.co.freemind.calico.service.Service;
import org.apache.commons.lang3.StringUtils;

public class UserInfoService extends Service {
  @Inject private UserInfoDao userInfoDao;

  public Boolean isUniqueLoginId(String loginId, Integer exceptId) {
    if(StringUtils.isEmpty(loginId)) return true;
    return userInfoDao.findForLoginIdUniqueCheck(loginId, exceptId).size() == 0;
  }
}
