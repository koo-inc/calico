package jp.co.freemind.calico.core.util;

import java.io.Serializable;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.google.common.base.Charsets;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.SneakyThrows;
import lombok.Value;

public class CryptoUtil implements Serializable {
  private static final String ALGORITHM = "AES";
  private static final String MODE = "CBC";
  private static final String PADDING = "PKCS5Padding";
  private static final String TRANSFORM = ALGORITHM + "/" + MODE + "/" + PADDING;
  private static final IvParameterSpec IV = new IvParameterSpec("8=QGPUclr^uTA8BZ".getBytes());


  /**
   * AESで暗号化します。
   * @param src
   * @return encrypted string
   */
  public static String encrypt(String src, String secretToken) {
    byte[] key = secretToken.getBytes();
    src = RandomUtil.randomAscii(key.length) + src;
    byte[] bytes = getDefaultCipherPair(key).encrypt(src.getBytes(Charsets.UTF_8));
    return Base64.getEncoder().encodeToString(bytes);
  }

  /**
   * AESで復号化します。
   * @param src
   * @return decrypted string
   */
  public static String decrypt(String src, String secretToken) {
    byte[] key = secretToken.getBytes();
    byte[] bytes = Base64.getDecoder().decode(src);
    try {
      bytes = getDefaultCipherPair(key).decrypt(bytes);
    } catch(Exception e) {
      cipherPairs.invalidate(new KeyWrapper(key));
      throw e;
    }
    return new String(bytes, Charsets.UTF_8).substring(key.length);
  }

  @SneakyThrows
  private static CipherPair getDefaultCipherPair(byte[] key) {
    return cipherPairs.get(new KeyWrapper(key));
  }

  /**
   * 暗号化、復号化するクラスです。
   * 暗号化Cipherと復号化Cipherを保持します。
   * Cipherはマルチスレッドでは使えないためsynchronizeしています。
   */
  private static final class CipherPair {
    public final Cipher encCipher;
    public final Cipher decCipher;
    public CipherPair(final byte[] keyData) {
      final java.security.Key key = new SecretKeySpec(keyData, ALGORITHM);
      try {
        encCipher = Cipher.getInstance(TRANSFORM);
        encCipher.init(Cipher.ENCRYPT_MODE, key, IV);
        decCipher = Cipher.getInstance(TRANSFORM);
        decCipher.init(Cipher.DECRYPT_MODE, key, IV);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    /**
     * 暗号化します。
     * @param src
     * @return
     */

    public byte[] encrypt(final byte[] src) {
      synchronized (encCipher) {
        try {
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

    public byte[] decrypt(final byte[] src) {
      synchronized (decCipher) {
        try {
          return decCipher.doFinal(src);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  /**
   * ChiperPairをキャッシュするためのキーです。
   */
  @Value private static final class KeyWrapper {
    private final byte[] keyData;
  }

  /**
   * CipherPairのキャッシュです。
   */
  private static final LoadingCache<KeyWrapper, CipherPair> cipherPairs = CacheBuilder.newBuilder()
    .maximumSize(10)
    .softValues()
    .expireAfterWrite(10, TimeUnit.MINUTES)
    .build(new CacheLoader<KeyWrapper, CipherPair>() {
      @Override public CipherPair load(KeyWrapper keyWrapper) throws Exception {
        return new CipherPair(keyWrapper.getKeyData());
      }
    });

}
