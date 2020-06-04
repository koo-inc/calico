package jp.co.freemind.calico.jackson.deser;

import java.io.IOException;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Strings;
import jp.co.freemind.calico.core.media.Media;
import jp.co.freemind.calico.core.media.MediaMeta;
import jp.co.freemind.calico.core.media.MediaProxy;
import jp.co.freemind.calico.core.media.MediaStorage;
import jp.co.freemind.calico.core.util.Throwables;
import jp.co.freemind.calico.core.di.InjectorRef;

/**
 * Created by tasuku on 15/04/30.
 */
public class MediaDeserializer extends JsonDeserializer<Media> {

  @Override
  public Media deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    if (p.getCurrentToken() != JsonToken.START_OBJECT) throw ctxt.wrongTokenException(p, JsonToken.START_OBJECT, "Expected object.");

    JsonNode node = p.getCodec().readTree(p);
    JsonNode metaNode = node.get("meta");

    Optional<String> id = getValueAsText(node.get("id"));
    if (id.isPresent()) {
      MediaStorage storage = InjectorRef.getInstance(MediaStorage.class);
      Media media = new MediaProxy(storage);
      media.setId(id.get());
      media.setMeta(deserializeMediaMeta(metaNode));
      return media;
    }

    Media media = new Media();
    media.setMeta(deserializeMediaMeta(metaNode));
    getValueAsBinary(node.get("payload")).ifPresent(media::setPayload);
    return media;
  }

  private MediaMeta deserializeMediaMeta(JsonNode metaNode) {
    MediaMeta meta = new MediaMeta();
    if (metaNode == null) return meta;

    getValueAsText(metaNode.get("name")).ifPresent(meta::setName);
    getValueAsText(metaNode.get("type")).ifPresent(meta::setType);
    getValueAsLong(metaNode.get("size")).ifPresent(meta::setSize);
    return meta;
  }

  private Optional<String> getValueAsText(JsonNode node) {
    if (node == null) return Optional.empty();
    String  value = node.textValue();
    return Strings.isNullOrEmpty(value) ? Optional.empty() : Optional.of(value);
  }

  private Optional<Long> getValueAsLong(JsonNode node) {
    if (node == null) return Optional.empty();
    return Optional.ofNullable(node.longValue());
  }

  private Optional<byte[]> getValueAsBinary(JsonNode node) {
    if (node == null) return Optional.empty();
    try {
      return Optional.ofNullable(node.binaryValue());
    } catch (IOException e) {
      throw Throwables.sneakyThrow(e);
    }
  }
}
