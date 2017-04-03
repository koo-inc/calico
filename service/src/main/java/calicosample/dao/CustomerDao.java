package calicosample.dao;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import calicosample.core.doma.InjectConfig;
import calicosample.endpoint.customer.SearchEndpoint;
import calicosample.entity.Customer;
import calicosample.entity.CustomerFamily;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.SelectType;
import org.seasar.doma.Update;
import org.seasar.doma.jdbc.SelectOptions;

/**
 * Created by tasuku on 15/03/10.
 */
@Dao
@InjectConfig
public interface CustomerDao {
  @Select
  List<Customer> findAll();
  @Select
  Customer findById(Customer.ID id);
  @Select(strategy = SelectType.STREAM)
  List<SearchEndpoint.Record> search(SearchEndpoint.Input form, SelectOptions options, Function<Stream<Customer>, List<SearchEndpoint.Record>> function);
  @Insert
  int insert(Customer customer);
  @Update
  int update(Customer customer);
  @Delete
  int delete(Customer customer);

  @Select
  List<CustomerFamily> findFamiliesByCustomer(Customer customer);
  @Select(strategy = SelectType.STREAM)
  Void findFamiliesByCustomer(Customer customer, Function<Stream<CustomerFamily>, Void> function);
  @Insert
  int insertFamily(CustomerFamily family);
  default int insertFamily(Customer customer, CustomerFamily family) {
    family.setCustomerId(customer.getId());
    return insertFamily(family);
  }
  @Update(excludeNull = true)
  int updateFamily(CustomerFamily family);
  @Delete
  int deleteFamily(CustomerFamily family);
}
