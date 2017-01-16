package jp.co.freemind.calico.config.freemarker;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.ext.beans.BeanModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import jp.co.freemind.calico.config.jackson.ObjectMapperProvider;
import lombok.SneakyThrows;

public class ToJson implements TemplateMethodModelEx {
  private ObjectMapper objectMapper = ObjectMapperProvider.createMapper();

  @SuppressWarnings("rawtypes")
  @Override
  @SneakyThrows
  public Object exec(List arguments) throws TemplateModelException {
    Object obj = arguments.get(0);
    if(obj instanceof BeanModel){
      BeanModel bm = (BeanModel)obj;
      return objectMapper.writeValueAsString(bm.getWrappedObject());
    }else{
      return objectMapper.writeValueAsString(obj);
    }
  }
}
