package jp.co.freemind.calico.csv;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.logging.log4j.util.Strings;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import jp.co.freemind.calico.core.media.Media;
import jp.co.freemind.calico.core.media.MediaMeta;
import jp.co.freemind.calico.core.util.Throwables;
import jp.co.freemind.csv.internal.CsvLineJoiner;
import jp.co.freemind.csv.internal.CsvLineParser;
import jp.co.freemind.csv.internal.CsvOutputStreamWriter;
import jp.co.freemind.csv.internal.CsvScanner;

public class CsvGrid {
  private final Media media;
  private final CsvMetaInfo metaInfo;
  private final Table<Integer, Integer, String> cellMessages;
  private final Map<Integer, String> lineMessages;

  public CsvGrid(Media media, CsvMetaInfo metaInfo) {
    this.media = media;
    this.metaInfo = metaInfo;
    this.cellMessages = HashBasedTable.create();
    this.lineMessages = new LinkedHashMap<>();
  }

  public CsvGrid mark(int lineIndex, int columnIndex, String message) {
    this.cellMessages.put(lineIndex, columnIndex, message);
    return this;
  }

  public CsvGrid mark(int lineIndex, String message) {
    this.lineMessages.put(lineIndex, message);
    return this;
  }

  public Media getMarkedMedia() {
    try {
      CsvFormatType formatType = metaInfo.getType();
      Charset charset = Charset.forName(formatType.charset());
      try (InputStream is = new ByteArrayInputStream(media.getPayload());
           CsvScanner scanner = new CsvScanner(is, charset, formatType.quoteChar(), formatType.escapeChar())) {
        CsvLineParser parser = new CsvLineParser(formatType.quoteChar(), formatType.escapeChar(), formatType.fieldSeparator());
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();
             CsvOutputStreamWriter writer = new CsvOutputStreamWriter(os, charset, formatType.lineBreak().getValue(), formatType.bomRequired())) {
          int index = metaInfo.isWithHeader() ? -1 : 0;
          int limit = metaInfo.getPaths().length;
          while (true) {
            String line = scanner.nextLine();
            if (line == null) break;
            CsvLineJoiner joiner = getJoiner(formatType);
            parser.parse(line).stream().limit(limit).forEach(joiner::append);
            joiner.append(getMessage(index));
            writer.write(joiner.toString());
            index++;
          }
          MediaMeta meta = new MediaMeta();
          meta.setType(formatType.mimeType());
          meta.setSize((long) os.size());
          meta.setName(media.getMeta().getName());
          Media newMedia = new Media();
          newMedia.setMeta(meta);
          newMedia.setPayload(os.toByteArray());
          return newMedia;
        }
      }
    } catch (final java.lang.Throwable $ex) {
      throw Throwables.sneakyThrow($ex);
    }
  }

  private CsvLineJoiner getJoiner(CsvFormatType formatType) {
    return new CsvLineJoiner(formatType.fieldSeparator(), formatType.quoteChar(), formatType.escapeChar(), formatType.nullValue(), formatType.bareFieldIfPossible());
  }

  private String getMessage(int index) {
    StringJoiner joiner = new StringJoiner("\n");
    if (lineMessages.containsKey(index)) {
      joiner.add(lineMessages.get(index));
    }
    cellMessages.row(index).forEach((columnIndex,message)->joiner.add("[" + metaInfo.getNames()[columnIndex] + "] " + message));
    return joiner.toString();
  }

  private String getLabel(int columnIndex) {
    if (columnIndex < 0) throw new IllegalArgumentException("invalid column index");
    if (columnIndex < metaInfo.getNames().length) {
      String name = metaInfo.getNames()[columnIndex];
      if (Strings.isNotEmpty(name)) {
        return "[" + name + "]";
      }
    }
    return "[" + columnIndex + "]";
  }
}
