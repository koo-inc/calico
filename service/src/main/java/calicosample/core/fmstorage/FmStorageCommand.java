package calicosample.core.fmstorage;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import jp.co.freemind.calico.core.di.InjectorRef;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import jp.co.freemind.calico.core.media.MediaMeta;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

/**
 * Created by tasuku on 15/04/27.
 */
@Log4j2
abstract class FmStorageCommand<T> {
  static final FmStorageCommand<InputStream> GET = new FmStorageCommand<InputStream>("get.php") {
    @Override protected InputStream doExecute(Map<String, Object> param) throws Exception {
      requires(param, "path");

      log(param.get("path"));

      try (InputStream is = read(param)) {
        return new ByteArrayInputStream(ByteStreams.toByteArray(is));
      }
    }
  };

  static final FmStorageCommand<List<String>> LIST = new FmStorageCommand<List<String>>("list.php") {
    @Override protected List<String> doExecute(Map<String, Object> param) throws Exception {
      requires(param, "path");

      log(param.get("path"));

      return readLinesAsString(param);
    }
  };

  static final FmStorageCommand<MediaMeta> META =  new FmStorageCommand<MediaMeta>("meta.php") {
    @Override protected MediaMeta doExecute(Map<String, Object> param) throws Exception {
      requires(param, "path");

      log(param.get("path"));

      List<String> lines = readLinesAsString(param);

      MediaMeta props = new MediaMeta();
      lines.stream()
        .map(line -> line.split("=", 2))
        .filter(pair -> pair.length == 2)
        .forEach(pair -> {
          switch (pair[0]) {
            case "name":
              props.setName(decodeUrl(pair[1])); // 文字化け対策・・・
              break;
            case "type":
              props.setType(pair[1]);
              break;
            case "size":
              if (!Strings.isNullOrEmpty(pair[1])) {
                props.setSize(Long.parseLong(pair[1]));
              }
              break;
          }
        });
      return props;
    }
  };

  static final FmStorageCommand<Void> PUT = new FmStorageCommand<Void>("put.php") {
    @Override protected Void doExecute(Map<String, Object> param) throws Exception {
      String path = requires(param, "path", String.class);
      String name = optional(param, "name", String.class, () -> path);
      String type = optional(param, "type", String.class, ContentType.APPLICATION_OCTET_STREAM::toString);
      byte[] data = requires(param, "data", byte[].class);

      log(param.get("path"));

      HttpPost post = new HttpPost(createPostUrl().toURI());
      HttpEntity entity = MultipartEntityBuilder.create()
        .addTextBody("path", path, ContentType.TEXT_PLAIN)
        .addBinaryBody("file", data, ContentType.create(type), encodeUrl(name)) // 文字化け対策・・・
        .build();
      post.setEntity(entity);

      try (CloseableHttpClient client = HttpClients.createDefault();
           CloseableHttpResponse res = client.execute(post)) {
        validateResponse(EntityUtils.toString(res.getEntity()));
      }
      return null;
    }
  };

  static final FmStorageCommand<Void> DELETE = new FmStorageCommand<Void>("delete.php") {
    @Override protected Void doExecute(Map<String, Object> param) throws Exception {
      requires(param, "path");

      log(param.get("path"));

      operate(param);
      return null;
    }
  };

  static final FmStorageCommand<Void> COPY = new FmStorageCommand<Void>("copy.php") {
    @Override
    protected Void doExecute(Map<String, Object> param) throws Exception {
      requires(param, "path");
      requires(param, "dest");

      log(param.get("path") + " TO " + param.get("dest"));

      operate(param);
      return null;
    }
  };

  private String action;
  private FmStorageCommand(String action) {
    this.action = action;
  }

  @SneakyThrows
  public T execute(Map<String, Object> param) {
    return doExecute(param);
  }

  abstract protected T doExecute(Map<String, Object> param) throws Exception;

  @SneakyThrows
  protected String toParamString(Map<String, Object> map) {
    return map.entrySet().stream()
      .filter(e -> e.getValue() != null)
      .map(e -> e.getKey() + "=" + encodeUrl(String.valueOf(e.getValue())))
      .collect(Collectors.joining("&"));
  }

  @SneakyThrows
  protected String encodeUrl(String str) {
    return URLEncoder.encode(str, getSetting().getCharset());
  }
  @SneakyThrows
  protected String decodeUrl(String str) {
    return URLDecoder.decode(str, getSetting().getCharset());
  }

  @SneakyThrows
  protected URL createGetUrl(Map<String, Object> param) {
    return new URL(getSetting().getUrl() + action + "?" + toParamString(param));
  }
  @SneakyThrows
  protected URL createPostUrl() {
    return new URL(getSetting().getUrl() + action);
  }

  @SneakyThrows
  protected InputStream read(Map<String, Object> param) {
    try (CloseableHttpClient client = HttpClients.createDefault();
         CloseableHttpResponse res = client.execute(new HttpGet(createGetUrl(param).toURI()))) {
      return new ByteArrayInputStream(ByteStreams.toByteArray(res.getEntity().getContent()));
    }
  }

  @SneakyThrows
  protected List<String> readLinesAsString(Map<String, Object> param) {
    try (InputStream is = read(param);
         InputStreamReader isr = new InputStreamReader(is)) {
      List<String> result = CharStreams.readLines(isr);
      if (result.size() > 0) {
        validateResponse(result.get(0));
      }
      return result;
    }
  }

  @SneakyThrows
  protected void operate(Map<String, Object> param) {
    HttpPost post = new HttpPost(createPostUrl().toURI());
    List<NameValuePair> values = param.entrySet().stream()
      .filter(e -> e.getValue() != null)
      .map(e -> new BasicNameValuePair(e.getKey(), String.valueOf(e.getValue())))
      .collect(Collectors.toList());
    post.setEntity(new UrlEncodedFormEntity(values));

    try (CloseableHttpClient client = HttpClients.createDefault();
         CloseableHttpResponse res = client.execute(post)) {
      validateResponse(EntityUtils.toString(res.getEntity()));
    }
  }

  protected void requires(Map<String, Object> param, String key) {
    if (!param.containsKey(key)) {
      throw new IllegalArgumentException(key + " is not in " + param.toString() + ".");
    }
  }

  @SuppressWarnings("unchecked")
  protected <R> R requires(Map<String, Object> param, String key, Class<R> clazz) {
    if (!param.containsKey(key)) {
      throw new IllegalArgumentException(key + " is not in " + param.toString() + ".");
    }
    if (!clazz.isAssignableFrom(param.get(key).getClass())) {
      throw new IllegalArgumentException(key + " is not instance of " + clazz.getSimpleName());
    }

    return (R) param.get(key);
  }

  protected <R> R optional(Map<String, Object> param, String key, Class<R> clazz, Supplier<R> sup) {
    if (!param.containsKey(key)) return sup.get();
    return requires(param, key, clazz);
  }

  protected void validateResponse(String content) {
    if (content.startsWith("error:")) {
      throw new RuntimeException(content);
    }
  }

  protected void log(Object message) {
    log.trace("[FmStorage] 実行コマンド " + action.replace(".php", "").toUpperCase() + " : " + message);
  }


  private FmStorageSetting getSetting() {
    return InjectorRef.getCurrent().getInstance(FmStorageSetting.class);
  }
}
