package jp.co.freemind.calico.core.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

import com.google.common.io.ByteSource;
import com.google.common.io.ByteStreams;
import com.google.common.io.FileBackedOutputStream;

public class FileBackedInputStream extends InputStream {
  private final int threshold;
  private ByteSource byteSource;
  private InputStream is;
  private int readlimit = -1;

  public FileBackedInputStream(Supplier<InputStream> supplier, int threshold) {
    this(supplier.get(), threshold);
  }

  public FileBackedInputStream(InputStream is, int threshold) {
    this.threshold = threshold;
    try (FileBackedOutputStream fbos = new FileBackedOutputStream(threshold)) {
      ByteStreams.copy(is, fbos);
      is.close();
      this.byteSource = fbos.asByteSource();
      this.is = byteSource.openBufferedStream();
    } catch (IOException e) {
      this.byteSource = ByteSource.empty();
      this.is = new ByteArrayInputStream(new byte[0]);
    }
  }

  FileBackedInputStream(FileBackedInputStream fis) throws IOException {
    this(fis, fis.threshold);
  }

  @Override
  public int read() throws IOException {
    return is.read();
  }

  @Override
  public int available() throws IOException {
    return is.available();
  }

  @Override
  public boolean markSupported() {
    return true;
  }

  @Override
  public long skip(long n) throws IOException {
    return is.skip(n);
  }

  @Override
  public void reset() throws IOException {
    is = byteSource.openStream();
    is.skip(readlimit);
  }

  @Override
  public int read(byte[] b) throws IOException {
    return is.read(b);
  }

  @Override
  public void close() throws IOException {
    is.close();
  }

  @Override
  public void mark(int readlimit) {
    this.readlimit = readlimit;
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    return is.read(b, off, len);
  }
}
