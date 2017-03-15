export const AppConfig = {
  appName: "calico-sample",
  envName: "${envName}",
  deployedAt: "${generated.format('yyyyMMddHHmmss')}",
  messages: {
  <%message.each {msg -> out << """
    "${msg.key}": "${msg.value}",
  """ }%>
  }
};
