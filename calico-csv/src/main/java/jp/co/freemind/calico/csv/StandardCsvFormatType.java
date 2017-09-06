package jp.co.freemind.calico.csv;

import jp.co.freemind.csv.CsvFormatter;

abstract class StandardCsvFormatType implements CsvFormatType {
  @Override public String charset() { return "UTF-8"; }
  @Override public boolean bomRequired() { return false; }
  @Override public char fieldSeparator() { return ','; }
  @Override public CsvFormatter.LineBreak lineBreak() { return CsvFormatter.LineBreak.CRLF; }
  @Override public char quoteChar() { return '"'; }
  @Override public char escapeChar() { return '"'; }
  @Override public String nullValue() { return ""; }
  @Override public boolean bareFieldIfPossible() { return false; }
  @Override public String mimeType() { return "text/csv"; }

  public static CsvFormatType CSV = new StandardCsvFormatType() {
    @Override public String charset() { return "MS932"; }
  };
  public static CsvFormatType TSV = new StandardCsvFormatType() {
    @Override public char fieldSeparator() { return '\t'; }
  };

}
