package calicosample.endpoint.customer;

import java.io.ByteArrayInputStream;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import calicosample.dao.CustomerDao;
import calicosample.util.CsvUtil;
import jp.co.freemind.calico.dto.DTOUtil;
import jp.co.freemind.calico.media.Media;
import jp.co.freemind.calico.service.exception.ServiceException;
import jp.co.freemind.calico.util.OptionalUtil;
import jp.co.freemind.csv.CsvMapper;
import jp.co.freemind.csv.CsvReader;
import jp.co.freemind.csv.Location;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

public class UploadFamilyCsvEndpoint extends CustomerEndpoint<UploadFamilyCsvEndpoint.Input, List<DownloadFamilyCsvEndpoint.CustomerFamilyRecord>> {
  @Inject private CustomerDao customerDao;
  private final CsvMapper<DownloadFamilyCsvEndpoint.CustomerFamilyRecord> csvMapper = CsvUtil.createMapperForDownload(DownloadFamilyCsvEndpoint.CustomerFamilyRecord.class, DownloadFamilyCsvEndpoint.CustomerFamilyRecord.CsvFormat.class);

  @Getter @Setter
  public static class Input {
    @NotNull
    private Media csv;

    @SneakyThrows
    public String getCsvContent() {
      return new String(csv.getPayload(), "UTF-8");
    }
  }

  @Override
  @SneakyThrows
  public List<DownloadFamilyCsvEndpoint.CustomerFamilyRecord> execute(Input input) {
    CsvReader<DownloadFamilyCsvEndpoint.CustomerFamilyRecord> reader = csvMapper.createReader();
    try (Stream<DownloadFamilyCsvEndpoint.CustomerFamilyRecord> stream = reader.read(new ByteArrayInputStream(input.getCsv().getPayload()))) {
      List<DownloadFamilyCsvEndpoint.CustomerFamilyRecord> records = stream.collect(Collectors.toList());
      if (reader.getErrorLocations().size() == 0) {
        List<DownloadFamilyCsvEndpoint.CustomerFamilyRecord> families = records.stream()
          .map(r -> DTOUtil.copyProperties(new DownloadFamilyCsvEndpoint.CustomerFamilyRecord(), r)).collect(Collectors.toList());
        families.forEach(f -> f.setId(null));
        return families;
      }
    }

    ServiceException exception = new ServiceException();

    reader.getErrorLocations().stream()
      .sorted(Comparator.comparingInt(Location::getLineNumber).thenComparingInt(l -> l.getColumnNumber().orElse(-1)))
      .map(l -> OptionalUtil.boxing(l.getColumnNumber())
        .map(i -> String.format("%d 行 %d 列目 の項目の形式が不正です", l.getLineNumber(), i))
        .orElseGet(() -> String.format("%d 行目 の形式が不正です", l.getLineNumber())))
      .forEach(message -> exception.add("csv", message));

    throw exception;
  }
}
