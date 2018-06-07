package jp.co.freemind.calico.core.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public final class RandomUtil {
  private static Random random;

  private static final String NUMERIC = "0123456789";
  private static final String LOWER_ALPHABET = "abcdefghijklmnopqrstuvwxyz";
  private static final String UPPER_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final String SYMBOL = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
  private static final String ALPHABET = LOWER_ALPHABET + UPPER_ALPHABET;
  private static final String ALPHANUMERIC = ALPHABET + NUMERIC;
  private static final String ASCII = ALPHANUMERIC + SYMBOL;

  static {
    try {
      random = SecureRandom.getInstanceStrong();
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }

  public static synchronized void setRandom(Random random) {
    RandomUtil.random = random;
  }

  public static String randomNumeric(int length) {
    return random(length, NUMERIC);
  }
  public static String randomAlphabet(int length) {
    return random(length, ALPHABET);
  }
  public static String randomAlphanumeric(int length) {
    return random(length, ALPHANUMERIC);
  }
  public static String randomAscii(int length) {
    return random(length, ASCII);
  }

  public static String random(int length, String str) {
    StringBuilder builder = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      int codePoint = str.codePointAt(random.nextInt(str.length()));
      builder.appendCodePoint(codePoint);
    }
    return builder.toString();
  }

  public static int randomInt() {
    return random.nextInt();
  }
  public static long randomLong() {
    return random.nextLong();
  }
}
