package jp.co.freemind.calico.csv;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import jp.co.freemind.calico.core.endpoint.validation.Validator;
import jp.co.freemind.calico.core.media.Media;
import jp.co.freemind.calico.core.media.MediaMeta;
import jp.co.freemind.calico.core.util.type.Tuple;
import jp.co.freemind.calico.core.util.type.Tuple2;
import jp.co.freemind.csv.CsvFormatter;
import jp.co.freemind.csv.CsvMapper;
import jp.co.freemind.csv.CsvReader;
import jp.co.freemind.csv.CsvWriter;
import jp.co.freemind.csv.Location;
import jp.co.freemind.csv.exception.ValidationException;
import lombok.Value;

public class CsvService {
  private final Validator validator;
  private final ObjectMapper objectMapper;
  private final CsvSetting setting;

  @Inject
  public CsvService(Validator validator, ObjectMapper objectMapper, CsvSetting setting) {
    this.validator = validator;
    this.objectMapper = objectMapper;
    this.setting = setting;
  }

  public <T> CsvResult<T> parse(Media media, Class<T> csvClass) {
    return parse(media, csvClass, csvClass);
  }

  public <T> CsvResult<T> parse(Media media, Class<T> csvClass, Class<T> formatClass) {
    return parse(media, csvClass, buildMetaInfo(formatClass));
  }

  public <T> CsvResult<T> parse(Media media, Class<T> csvClass, CsvMetaInfo metaInfo) {
    Tuple2<List<T>, Map<Location, String>> result = getLines(media, csvClass, metaInfo);
    return new CsvResult<>(result.getValue1(), result.getValue2(), setting.getFormatError());
  }

  public <T> Media build(String filename, Class<T> csvClass, Collection<T> collection) {
    return build(filename, csvClass, csvClass, collection.stream());
  }
  public <T> Media build(String filename, Class<T> csvClass, Stream<T> stream) {
    return build(filename, csvClass, csvClass, stream);
  }
  public <T, U> Media build(String filename, Class<T> csvClass, Class<U> formatClass, Collection<T> collection) {
    return build(filename, csvClass, buildMetaInfo(formatClass), collection);
  }
  public <T, U> Media build(String filename, Class<T> csvClass, Class<U> formatClass, Stream<T> stream) {
    return build(filename, csvClass, buildMetaInfo(formatClass), stream);
  }
  public <T> Media build(String filename, Class<T> csvClass, CsvMetaInfo metaInfo, Collection<T> collection) {
    return build(filename, csvClass, metaInfo, collection.stream());
  }
  public <T> Media build(String filename, Class<T> csvClass, CsvMetaInfo metaInfo, Stream<T> stream) {
    CsvWriter<T> writer = getMapper(csvClass, metaInfo).createWriter();
    if (metaInfo.isWithHeader()) {
      writer = writer.withHeader(metaInfo.getNames());
    }
    try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
      stream.forEach(writer.writeTo(os));
      stream.close();
      MediaMeta meta = new MediaMeta();
      meta.setName(filename);
      meta.setSize((long) os.size());
      meta.setType(metaInfo.getType().mimeType());
      Media media = new Media();
      media.setMeta(meta);
      media.setPayload(os.toByteArray());
      return media;
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public CsvGrid createGrid(Media media, Class<?> formatClass) {
    return this.createGrid(media, buildMetaInfo(formatClass));
  }
  public CsvGrid createGrid(Media media, CsvMetaInfo metaInfo) {
    return new CsvGrid(media, metaInfo);
  }

  private <T> Tuple2<List<T>, Map<Location, String>> getLines(Media media, Class<T> csvClass, CsvMetaInfo metaInfo) {
    CsvReader<T> reader = getMapper(csvClass, metaInfo).createReader();
    try (ByteArrayInputStream is = new ByteArrayInputStream(media.getPayload())) {
      List<T> lines = reader.read(is, line -> {
        Map<String, String> violation = validator.validate(csvClass, line).toMap().entrySet().stream()
          .collect(toMap(Map.Entry::getKey, e -> e.getValue().stream().findFirst().orElse(null)));
        throw new ValidationException(violation);
      }).collect(toList());
      return Tuple.of(lines, reader.getErrors());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public <T> CsvMapper<T> getMapper(Class<T> csvClass, CsvMetaInfo metaInfo) {
    return new CsvMapper<>(buildFormatter(csvClass, metaInfo), objectMapper);
  }

  public <T> CsvFormatter<T> buildFormatter(Class<T> csvClass, CsvMetaInfo metaInfo) {
    CsvFormatter.Builder<T> builder = CsvFormatter.builder(csvClass)
      .charset(metaInfo.getType().charset())
      .columnSeparator(metaInfo.getType().fieldSeparator())
      .escapeChar(metaInfo.getType().escapeChar())
      .lineBreak(metaInfo.getType().lineBreak())
      .quoteChar(metaInfo.getType().quoteChar())
      .orderBy(metaInfo.getPaths())
      ;

    if (metaInfo.getFormatClass() != null) {
      builder.with(metaInfo.getFormatClass());
    }

    if (metaInfo.isWithHeader()) {
      builder.withHeaders();
    }
    else {
      builder.withoutHeader();
    }

    return builder.build();
  }

  public CsvMetaInfo buildMetaInfo(Class<?> formatClass) {
    CsvMetaInfo metaInfo = new CsvMetaInfo()
      .format(formatClass);

    List<Column> columns = Stream.of(formatClass.getDeclaredFields())
      .map(Column::of)
      .filter(Objects::nonNull)
      .sorted(Comparator.comparing(Column::getOrder).thenComparing(Column::getName))
      .collect(toList());

    for (Column column : columns) {
      metaInfo = metaInfo.col(column.path, column.name);
    }

    return metaInfo;
  }

  @Value
  private static class Column {
    private final int order;
    private final String path;
    private final String name;

    static Column of(Field field) {
      CsvColumn csvColumn = field.getAnnotation(CsvColumn.class);
      if (csvColumn == null) return null;
      return new Column(csvColumn.order(), field.getName(), csvColumn.name());
    }
  }
}
