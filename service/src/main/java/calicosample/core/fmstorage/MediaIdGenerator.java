package calicosample.core.fmstorage;

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import jp.co.freemind.calico.core.media.Media;

import java.nio.ByteBuffer;
import java.util.Base64;

/**
 * Created by kakusuke on 15/05/12.
 */
public interface MediaIdGenerator {

  static String customerPhoto(Media media) {
    return "customer/photo/" + hashing(media);
  }

  static String hashing(Media media) {
    Hasher hasher = Hashing.sha1().newHasher();
    String hashed = hasher.putBytes(media.getPayload()).hash().toString();
    byte[] time = ByteBuffer.allocate(Long.BYTES).putLong(System.nanoTime()).array();
    String base64Time = Base64.getEncoder().encodeToString(time).replaceAll("[+=/]", "-");
    return hashed + "/" + base64Time;
  }
}
