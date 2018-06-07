package jp.co.freemind.calico.core.util;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.google.common.base.Charsets;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

public class AESCryptor {
  private static final String ALGORITHM = "AES";
  private static final String MODE = "CBC";
  private static final String PADDING = "PKCS5Padding";
  private static final String TRANSFORM = ALGORITHM + "/" + MODE + "/" + PADDING;

  private static final long UNIQUE_ID = RandomUtil.randomLong();
  private static final long START_TIME = System.currentTimeMillis();
  private static final AtomicLong COUNTER = new AtomicLong(0);

  private final String secretToken;
  private final Supplier<Hasher> hasher;
  private CipherPair cipherPair;

  public AESCryptor(String secretToken, Supplier<Hasher> hasher) {
    this.secretToken = secretToken;
    this.hasher = hasher;
    this.cipherPair = new CipherPair(secretToken.getBytes());
  }

  public AESCryptor(String secretToken) {
    this(secretToken, ()-> Hashing.sha256().newHasher());
  }

  /**
   * AESで暗号化します。
   * @param src
   * @return encrypted string
   */
  public String encrypt(String src) {
    byte[] key = secretToken.getBytes();
    byte[] iv = getIV();
    byte[] bytes = cipherPair.encrypt(src.getBytes(Charsets.UTF_8), iv);
    Base64.Encoder encoder = Base64.getEncoder();
    return encoder.encodeToString(iv) + "." + encoder.encodeToString(bytes) + "." + hashing(src.getBytes(), iv, key);
  }

  /**
   * AESで復号化します。
   * @param encrypted
   * @return decrypted string
   */
  public String decrypt(String encrypted) {
    byte[] key = secretToken.getBytes();
    try {
      String[] parts = encrypted.split("[.]");
      if (parts.length != 3) return "";
      Base64.Decoder decoder = Base64.getDecoder();
      byte[] iv = decoder.decode(parts[0]);
      byte[] bytes = decoder.decode(parts[1]);
      String signature = parts[2];
      String decoded = new String(cipherPair.decrypt(bytes, iv), Charsets.UTF_8);
      if (!signature.equals(hashing(decoded.getBytes(), iv, key))) return "";
      return decoded;
    } catch(Exception e) {
      return "";
    }
  }

  private byte[] getIV() {
    byte[] firstHalf = ByteBuffer.allocate(8).putLong(UNIQUE_ID).array();
    byte[] secondHalf = ByteBuffer.allocate(8).putLong(START_TIME + COUNTER.getAndIncrement()).array();
    byte[] ret = new byte[16];
    System.arraycopy(firstHalf, 0, ret, 0, firstHalf.length);
    System.arraycopy(secondHalf, 0, ret, 8, secondHalf.length);
    return ret;
  }

  private String hashing(byte[] body, byte[] iv, byte[] key) {
    return hasher.get().putBytes(body).putBytes(iv).putBytes(key).hash().toString();
  }

  /**
   * 暗号化、復号化するクラスです。
   * 暗号化Cipherと復号化Cipherを保持します。
   * Cipherはマルチスレッドでは使えないためsynchronizeしています。
   */
  static final class CipherPair {
    final Cipher encCipher;
    final Cipher decCipher;
    final java.security.Key key;
    public CipherPair(final byte[] keyData) {
      key = new SecretKeySpec(keyData, ALGORITHM);
      try {
        encCipher = Cipher.getInstance(TRANSFORM);
        decCipher = Cipher.getInstance(TRANSFORM);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    /**
     * 暗号化します。
     * @param src
     * @return
     */

    public byte[] encrypt(final byte[] src, byte[] iv) {
      synchronized (encCipher) {
        try {
          encCipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
          return encCipher.doFinal(src);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }
    /**
     * 復号化します。
     * @param src
     * @return
     */

    public byte[] decrypt(final byte[] src, byte[] iv) {
      synchronized (decCipher) {
        try {
          decCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
          return decCipher.doFinal(src);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }
  }
}
