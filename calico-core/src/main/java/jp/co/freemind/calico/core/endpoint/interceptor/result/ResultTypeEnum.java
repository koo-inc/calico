package jp.co.freemind.calico.core.endpoint.interceptor.result;

enum ResultTypeEnum implements ResultType {
  JSON("application/json");

  private final String mimeType;
  ResultTypeEnum(String mimeType) {
    this.mimeType = mimeType;
  }

  @Override
  public int getStatus() { return 200; }

  @Override
  public String getMimeType() { return mimeType; }
}
