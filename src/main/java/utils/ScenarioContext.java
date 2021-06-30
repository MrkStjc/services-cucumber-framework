package utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ScenarioContext {

  private Map<String, Object> contextMap = new HashMap<>();

  @SuppressWarnings("unchecked")
  public  <T> T get(String key) {
    return (T) contextMap.get(key);
  }

  public void put(String key, Object value) {
    contextMap.put(key, value);
  }

  public void clear() {
    contextMap.clear();
  }

  public boolean contains(String key) {
    return contextMap.containsKey(key);
  }

}
