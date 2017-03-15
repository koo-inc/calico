package jp.co.freemind.calico.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.fasterxml.jackson.datatype.jdk8.PackageVersion;
import jp.co.freemind.calico.jackson.deser.JsonObjectDeserializer;
import jp.co.freemind.calico.jackson.ser.JsonObjectSerializer;

/**
 * Created by kakusuke on 15/08/07.
 */
public class JsonObjectModule extends Module {
  @Override
  public void setupModule(SetupContext context) {
    context.addSerializers(new Serializers.Base() {
      @Override
      public JsonSerializer<?> findSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc) {
        Class<?> raw = type.getRawClass();
        if (JsonObject.class.isAssignableFrom(raw)) {
          return new JsonObjectSerializer();
        }
        return super.findSerializer(config, type, beanDesc);
      }
    });

    context.addDeserializers(new Deserializers.Base() {
      @Override
      public JsonDeserializer<?> findBeanDeserializer(JavaType type, DeserializationConfig config, BeanDescription beanDesc) throws JsonMappingException {
        Class<?> raw = type.getRawClass();
        if (JsonObject.class.isAssignableFrom(raw)) {
          return new JsonObjectDeserializer(type);
        }
        return super.findBeanDeserializer(type, config, beanDesc);
      }
    });
  }

  @Override
  public Version version() {
    return PackageVersion.VERSION;
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  public boolean equals(Object o) {
    return this == o;
  }

  @Override
  public String getModuleName() {
    return "jsonObjectModule";
  }
}
