package jp.co.freemind.calico.core.endpoint;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.freemind.calico.core.endpoint.aop.EndpointInvocation;
import jp.co.freemind.calico.core.endpoint.aop.InterceptionHandler;
import jp.co.freemind.calico.core.exception.InvalidUserDataException;
import jp.co.freemind.calico.core.exception.UnknownEndpointException;

public class Dispatcher {
  private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(Dispatcher.class);
  private static ObjectMapper mapper;

  private final EndpointResolver resolver;

  public static void init(ObjectMapper mapper) {
    Dispatcher.mapper = mapper.copy().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  public Dispatcher(EndpointResolver resolver) {
    this.resolver = resolver;
  }

  @SuppressWarnings("unchecked")
  public Object dispatch(String path, InputStream is, InterceptionHandler... handlers) throws UnknownEndpointException, Throwable {
    Class<? extends Endpoint<?, ?>> endpointClass = resolver.getEndpointClass(path)
      .orElseThrow(() -> new UnknownEndpointException(path));

    Object input = resolver.getInputType(endpointClass).map(inputType -> getInput(inputType, is)).orElse(null);
    return new EndpointInvocation(resolver, endpointClass, input, handlers).proceed();
  }

  private Object getInput(Class<?> inputType, InputStream input) {
    try (InputStream is = input){
      return mapper.readValue(is, inputType);
    } catch (IOException e) {
      try {
        if (Collection.class.isAssignableFrom(inputType)) {
          return mapper.readValue("[]", inputType);
        } else {
          return mapper.readValue("{}", inputType);
        }
      } catch (Exception e2) {
        log.info(e);
        throw new InvalidUserDataException("Internal Server Error", e);
      }
    }
  }
}
