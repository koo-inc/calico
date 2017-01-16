package jp.co.freemind.calico.config.beanutils;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.SuppressPropertiesBeanIntrospector;

public class SuppressPropertiesInitializer {

  public static void init() {
    // BeanUtils脆弱性対策
    // http://qiita.com/kawasima/items/404baa98984e7d7ca951
    PropertyUtils.addBeanIntrospector(SuppressPropertiesBeanIntrospector.SUPPRESS_CLASS);
    PropertyUtils.clearDescriptors();
  }
}
