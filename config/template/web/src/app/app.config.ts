export const AppConfig = {
  appName: "calico-sample",
  envName: "${envName}",
  version: "${generated.format('yyyyMMddHHmmss')}",
  messages: {
  <%message.each {msg -> out << """
    "${msg.key}": "${msg.value}",
  """ }%>
  }
};
