package jp.co.freemind.calico.jersey.frontmodule;

import jp.co.freemind.calico.jersey.FrontModule;

import java.util.Arrays;
import java.util.List;

/**
 * Created by tasuku on 15/02/23.
 */
public interface AllPartials extends FrontModule {

  @Override
  default List<String> getPartials() {
    return Arrays.asList(".");
  }
}
