package helper

class EnvironmentVariables {
  private static Class<?> envClass;
  private static Map<String, String> original

  EnvironmentVariables() {
    envClass = java.lang.ProcessEnvironment
    original = new HashMap<>(envClass.theUnmodifiableEnvironment as Map)
    reset()
  }

  def set(String key, String value) {
    editableMap.put(key, value)
  }

  def reset() {
    editableMap.clear()
    editableMap.putAll(original)
  }

  private Map getEditableMap() {
    def field = envClass.theUnmodifiableEnvironment.getClass().getDeclaredField('m')
    field.setAccessible(true)
    return field.get(envClass.theUnmodifiableEnvironment)
  }
}
