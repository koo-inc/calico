package jp.co.freemind.calico.csv;

import jp.co.freemind.csv.CsvFormatter;

public interface CsvFormatType {
  String charset();
  boolean bomRequired();
  char fieldSeparator();
  CsvFormatter.LineBreak lineBreak();
  char quoteChar();
  char escapeChar();
  String nullValue();
  boolean bareFieldIfPossible();
  String mimeType();
}
