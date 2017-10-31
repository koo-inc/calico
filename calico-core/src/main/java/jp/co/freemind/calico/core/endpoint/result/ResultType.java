package jp.co.freemind.calico.core.endpoint.result;

public interface ResultType {
  ResultType JSON = ResultTypeEnum.JSON;

  int getStatus();
  String getMimeType();

  static ResultType invalid(ResultType resultType) {
    return new AbnormalResultType(400, resultType);
  }
  static ResultType forbidden(ResultType resultType) {
    return new AbnormalResultType(403, resultType);
  }
  static ResultType notFound(ResultType resultType) {
    return new AbnormalResultType(404, resultType);
  }
  static ResultType conflict(ResultType resultType) {
    return new AbnormalResultType(409, resultType);
  }
  static ResultType error(ResultType resultType) {
    return new AbnormalResultType(500, resultType);
  }
}
