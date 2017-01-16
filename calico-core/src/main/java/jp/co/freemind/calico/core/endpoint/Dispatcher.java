package jp.co.freemind.calico.core.endpoint;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.freemind.calico.core.endpoint.aop.EndpointInvocation;
import jp.co.freemind.calico.core.endpoint.aop.InterceptionHandler;
import jp.co.freemind.calico.core.endpoint.exception.UnknownEndpointException;

public class Dispatcher {
  private static ObjectMapper mapper;

  private final EndpointResolver resolver;

  public static void init(ObjectMapper mapper) {
    Dispatcher.mapper = mapper.copy().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  public Dispatcher(EndpointResolver resolver) {
    this.resolver = resolver;
  }

  @SuppressWarnings("unchecked")
  public void dispatch(String path, InputStream is, InterceptionHandler... handlers) throws Throwable {
    Class<? extends Endpoint<?, ?>> endpointClass = resolver.getEndpointClass(path)
      .orElseThrow(() -> new UnknownEndpointException(path));

    Object input = resolver.getInputType(endpointClass).map(inputType -> getInput(inputType, is)).orElse(null);
    new EndpointInvocation(resolver, endpointClass, input, handlers).proceed();
  }

  private Object getInput(Class<?> inputType, InputStream input) {
    try (InputStream is = input){
      return mapper.readValue(is, inputType);
    } catch (IOException e) {
      try {
        return mapper.readValue("{}", inputType);
      } catch (IOException e2) {
        try {
          return mapper.readValue("[]", inputType);
        } catch (IOException e3) {
          return null;
        }
      }
    }
  }
}
