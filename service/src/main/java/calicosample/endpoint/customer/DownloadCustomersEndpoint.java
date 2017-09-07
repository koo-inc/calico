package calicosample.endpoint.customer;

import javax.inject.Inject;

import calicosample.dao.CustomerDao;
import calicosample.entity.Customer;
import jp.co.freemind.calico.core.media.Media;
import jp.co.freemind.calico.csv.CsvService;
import jp.co.freemind.calico.jackson.media.WithPayload;
import lombok.Value;

public class DownloadCustomersEndpoint extends CustomerEndpoint<SearchEndpoint.Input, DownloadCustomersEndpoint.Output> {
  @Inject private CustomerDao customerDao;
  @Inject private CsvService csvService;

  @Override
  public Output execute(SearchEndpoint.Input input) {
    Media csv = csvService.build("顧客情報.csv", Customer.class, CustomerCsvFormat.class, customerDao.search(input));
    return new Output(csv);
  }

  @Value
  public static class Output {
    @WithPayload
    private Media csv;
  }

}
