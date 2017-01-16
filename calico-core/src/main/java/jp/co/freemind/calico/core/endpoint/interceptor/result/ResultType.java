package jp.co.freemind.calico.core.endpoint.interceptor.result;

public interface ResultType {
  ResultType JSON = ResultTypeEnum.JSON;

  int getStatus();
  String getMimeType();

  static ResultType invalid(ResultType resultType) {
    return new InvalidResultType(resultType);
  }
}
