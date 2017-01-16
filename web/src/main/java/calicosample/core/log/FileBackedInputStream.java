package calicosample.core.log;

import java.io.*;

import com.google.common.io.ByteSource;
import com.google.common.io.ByteStreams;
import com.google.common.io.FileBackedOutputStream;

/**
 * Created by kakusuke on 15/06/17.
 */
class FileBackedInputStream extends InputStream {
  private final int threathold;
  private final ByteSource byteSource;
  private InputStream is;
  private int readlimit = -1;

  FileBackedInputStream(InputStream is, int threshold) throws IOException {
    this.threathold = threshold;
    try (FileBackedOutputStream fbos = new FileBackedOutputStream(threshold)) {
      ByteStreams.copy(is, fbos);
      byteSource = fbos.asByteSource();
    }
    this.is = byteSource.openBufferedStream();
  }

  FileBackedInputStream(FileBackedInputStream fis) throws IOException {
    this(fis, fis.threathold);
  }

  public int read() throws IOException {
    return is.read();
  }

  public int available() throws IOException {
    return is.available();
  }

  public boolean markSupported() {
    return true;
  }

  public long skip(long n) throws IOException {
    return is.skip(n);
  }

  public void reset() throws IOException {
    is = byteSource.openStream();
    is.skip(readlimit);
  }

  public int read(byte[] b) throws IOException {
    return is.read(b);
  }

  public void close() throws IOException {
    is.close();
  }

  public void mark(int readlimit) {
    this.readlimit = readlimit;
  }

  public int read(byte[] b, int off, int len) throws IOException {
    return is.read(b, off, len);
  }
}
