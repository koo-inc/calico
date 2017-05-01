package jp.co.freemind.calico.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.datatype.jdk8.PackageVersion;
import jp.co.freemind.calico.core.extenum.ExtEnum;
import jp.co.freemind.calico.jackson.deser.ExtEnumDeserializer;

public class ExtEnumModule extends Module {
  @Override
  public void setupModule(SetupContext context) {
    context.addDeserializers(new Deserializers.Base() {
      @Override
      @SuppressWarnings("unchecked")
      public JsonDeserializer<?> findBeanDeserializer(JavaType type, DeserializationConfig config, BeanDescription beanDesc) throws JsonMappingException {
        Class<?> raw = type.getRawClass();
        if (ExtEnum.class.isAssignableFrom(raw)) {
          return new ExtEnumDeserializer((Class<ExtEnum<?>>) type.getRawClass());
        }
        return super.findBeanDeserializer(type, config, beanDesc);
      }

      @Override
      @SuppressWarnings("unchecked")
      public JsonDeserializer<?> findEnumDeserializer(Class<?> type, DeserializationConfig config, BeanDescription beanDesc) throws JsonMappingException {
        if (ExtEnum.class.isAssignableFrom(type)) {
          return new ExtEnumDeserializer((Class<ExtEnum<?>>) type);
        }
        return super.findEnumDeserializer(type, config, beanDesc);
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
    return "extEnumModule";
  }
}
