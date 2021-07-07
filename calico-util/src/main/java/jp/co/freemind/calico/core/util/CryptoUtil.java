package jp.co.freemind.calico.core.util;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

public class CryptoUtil implements Serializable {
  private static Supplier<Hasher> HASHER = ()-> Hashing.sha256().newHasher();

  /**
   * 署名のアルゴリズムを変更できます。
   * @param hasher
   */
  public static void init(Supplier<Hasher> hasher) {
    HASHER = hasher;
  }

  /**
   * AESで暗号化します。
   * @param src
   * @return encrypted string
   */
  public static String encrypt(String src, String secretToken) {
    AESCryptor cryptor;
    try {
      cryptor = CryptoUtil.cryptors.get(secretToken);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
    return cryptor.encrypt(src);
  }

  /**
   * AESで復号化します。
   * @param encrypted
   * @return decrypted string
   */
  public static String decrypt(String encrypted, String secretToken) {
    AESCryptor cryptor;
    try {
      cryptor = CryptoUtil.cryptors.get(secretToken);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
    return cryptor.decrypt(encrypted);
  }

  /**
   * AESCryptorのキャッシュです。
   */
  private static final LoadingCache<String, AESCryptor> cryptors = CacheBuilder.newBuilder()
    .maximumSize(10)
    .softValues()
    .expireAfterWrite(10, TimeUnit.MINUTES)
    .build(new CacheLoader<String, AESCryptor>() {
      @Override public AESCryptor load(@Nonnull String secretToken) {
        return new AESCryptor(secretToken, HASHER);
      }
    });
}
