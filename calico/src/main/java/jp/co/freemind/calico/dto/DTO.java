package jp.co.freemind.calico.dto;

import jp.co.freemind.calico.guice.InjectUtil;

public abstract class DTO {
  public DTO(){
    InjectUtil.injectMembers(this);
  }
}
