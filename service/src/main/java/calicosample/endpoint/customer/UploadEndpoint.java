package calicosample.endpoint.customer;

import javax.inject.Inject;

import calicosample.service.CustomerService;
import jp.co.freemind.calico.core.media.Media;
import jp.co.freemind.calico.csv.CsvGrid;
import jp.co.freemind.calico.csv.CsvResult;
import jp.co.freemind.calico.csv.CsvService;
import jp.co.freemind.calico.jackson.media.WithPayload;
import lombok.Data;
import lombok.Value;

public class UploadEndpoint extends CustomerEndpoint<UploadEndpoint.Input, UploadEndpoint.Output> {
  @Inject private CustomerService customerService;
  @Inject private CsvService csvService;

  @Override
  public Output execute(Input input) {
    CsvResult<CustomerCsvFormat> result = csvService.parse(input.getCsv(), CustomerCsvFormat.class);
    if (result.hasError()) {
      CsvGrid grid = csvService.createGrid(input.getCsv(), CustomerCsvFormat.class);
      result.getErrors().forEach((l, s) -> {
        if (l.getColumnNumber().isPresent()) {
          grid.mark(l.getLineIndex(), l.getColumnIndex(), s);
        }
        else {
          grid.mark(l.getLineIndex(), s);
        }
      });
      return new Output(result.getLines().size(), result.hasError(), grid.getMarkedMedia());
    }
    return new Output(result.getLines().size(), false, null);
  }

  @Data
  public static class Input {
    private Media csv;
  }

  @Value
  public static class Output {
    private Integer count;
    private boolean error;
    @WithPayload
    private Media file;
  }
}
