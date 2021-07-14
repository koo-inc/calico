package jp.co.freemind.calico.core.util

import spock.lang.Shared
import spock.lang.Specification

import java.util.stream.Collectors
import java.util.stream.IntStream


class CryptoUtilTest extends Specification {
  @Shared
  def secretToken = "a" * (128 / 8)

  def "暗号化後複合できること"() {
    when:
    def encrypted = CryptoUtil.encrypt(plainText, secretToken)

    then:
    assert plainText == CryptoUtil.decrypt(encrypted, secretToken)

    where:
    plainText          | _
    ""                 | _
    "H"                | _
    "He"               | _
    "Hel"              | _
    "Hell"             | _
    "Hello"            | _
    "Hello W"          | _
    "Hello Wo"         | _
    "Hello Wor"        | _
    "Hello Worl"       | _
    "Hello World"      | _
    "Hello World!"     | _
    "Hello World!!"    | _
    "Hello World!!!"   | _
    "Hello World!!!!"  | _
    "Hello World!!!!!" | _
  }

  def "thread safe"() {
    when:
    def result = IntStream.range(0, 1024).parallel()
      .mapToObj() {i ->
        CryptoUtil.decrypt(CryptoUtil.encrypt(String.valueOf(i), secretToken), secretToken);
        return Thread.currentThread()
      }.toArray()

    then:
    notThrown(Exception)
    and:
    assert result.size() == 1024
    and:
    assert !result.contains("")
  }

  static {
    RandomUtil.setRandom(new Random(2))
  }

  @Shared
  def target = CryptoUtil.encrypt("AAAAAAAAAAAAAAAABBBBBBBBBBBBBBBB", secretToken)

  def "padding oracle attack"() {
    when:
    def oracle = null
    try {
      def ret = CryptoUtil.decrypt(sniffer, secretToken)
      if (ret != "") {
        oracle = x
        print(" invalid " + x)
      }
    } catch (Exception e) {
      print(" invalid " + e.getMessage())
    }

    then:
    assert oracle != x

    where:
    x << IntStream.range(0, 256).mapToObj() {c -> String.format("%02x", c)}.collect(Collectors.toList())
    sniffer << IntStream.range(0, 256).mapToObj() {c ->
      def parts = target.split("[.]")
      def bytes = Base64.decoder.decode(parts[1])
      bytes = xor(bytes, [0, 0, 0, 0, 0, 0, 0, 1] as byte[], bytes.length - 16)
      bytes = xor(bytes, [0, 0, 0, 0, 0, 0, 0, c] as byte[], bytes.length - 16)
      return [parts[0], Base64.encoder.encodeToString(bytes), parts[2]].join('.')
    }.collect(Collectors.toList())
  }

  def xor(byte[] arr, byte[] mask, int start) {
    def ret = new byte[arr.length]
    if (start > 0) {
      System.arraycopy(arr, 0, ret, 0, start - 1)
    }
    for (int i = 0; i < mask.length; i++) {
      ret[start + i] = (byte) arr[start + i] ^ mask[i]
    }

    int index = start + mask.length
    System.arraycopy(arr, index, ret, index, arr.length - index)
    return ret
  }
}
