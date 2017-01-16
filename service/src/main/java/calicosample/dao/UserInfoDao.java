package calicosample.dao;

import calicosample.core.doma.InjectConfig;
import calicosample.entity.UserInfo;
import org.seasar.doma.*;

import java.util.List;
import java.util.Optional;

/**
 * Created by tasuku on 15/03/10.
 */
@Dao
@InjectConfig
public interface UserInfoDao {
  @Select(ensureResult = true)
  UserInfo findById(Integer id);

  @Select
  List<UserInfo> findAll();

  @Select
  Optional<UserInfo> findForLogin(String loginId, String password);

  @Select
  List<UserInfo> findForLoginIdUniqueCheck(String loginId, Integer exceptId);

  default List<UserInfo> findForLoginIdUniqueCheck(String loginId) {
    return findForLoginIdUniqueCheck(loginId, null);
  }

  @Insert
  int isnert(UserInfo userInfo);

  @Update
  int update(UserInfo userInfo);

  @Delete
  int delete(UserInfo userInfo);
}
