package jp.co.freemind.calico.csv;

import javax.annotation.Nullable;

import com.google.common.collect.ObjectArrays;

public class CsvMetaInfo {
  private final String[] paths;
  private final String[] names;
  private final CsvFormatType type;
  private final boolean withHeader;
  @Nullable
  private final Class<?> formatClass;

  public CsvMetaInfo() {
    this(new String[0], new String[0]);
  }

  public CsvMetaInfo(String[] paths, String[] names) {
    this(paths, names, StandardCsvFormatType.CSV, true, null);
  }

  CsvMetaInfo(String[] paths, String[] names, CsvFormatType type, boolean withHeader, Class<?> formatClass) {
    this.paths = paths;
    this.names = names;
    this.type = type;
    this.withHeader = withHeader;
    this.formatClass = formatClass;
  }

  public CsvMetaInfo col(String path, String name) {
    String[] paths = ObjectArrays.concat(this.paths, path);
    String[] names = ObjectArrays.concat(this.names, name);
    return new CsvMetaInfo(paths, names, type, withHeader, formatClass);
  }

  public CsvMetaInfo type(CsvFormatType type) {
    return new CsvMetaInfo(paths, names, type, withHeader, formatClass);
  }

  public CsvMetaInfo withHeader(boolean withHeader) {
    return new CsvMetaInfo(paths, names, type, withHeader, formatClass);
  }

  public CsvMetaInfo format(Class<?> formatClass) {
    return new CsvMetaInfo(paths, names, type, withHeader, formatClass);
  }

  public String[] getPaths() {
    return this.paths;
  }

  public String[] getNames() {
    return this.names;
  }

  public CsvFormatType getType() {
    return this.type;
  }

  public boolean isWithHeader() {
    return this.withHeader;
  }

  @Nullable
  public Class<?> getFormatClass() {
    return this.formatClass;
  }
}
