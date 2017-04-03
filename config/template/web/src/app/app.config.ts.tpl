export const AppConfig = {
  appName: "calico-sample",
  envName: "${envName}",
  version: "${generated.format('yyyyMMddHHmmss')}",
  versionTag: "${versionTag}",
  messages: {
  <%message.each {msg -> out << """
    "${msg.key}": "${msg.value}",
  """ }%>
  }
};
