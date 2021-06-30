package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataProvider<T> {

  @Autowired
  private Yaml yaml;

  @Autowired
  private ObjectMapper objectMapper;

  private Class<T> testDataType;
  private String testDataPath;

  public DataProvider(String path, Class<T> clazz) {
    testDataType = clazz;
    testDataPath = path;
  }

  public T getData(String name) {
    InputStream inputStream = this.getClass()
            .getClassLoader()
            .getResourceAsStream(testDataPath);
    Map<String, Object> yamlLoaded = yaml.load(inputStream);
    LinkedHashMap<String, Object> data = customize(yamlLoaded);
    if (!data.containsKey(name)) {
      throw new IllegalArgumentException("Data with name " + name + " is not present in " + testDataPath);
    }
    return objectMapper.convertValue(data.get(name), testDataType);
  }

  private static LinkedHashMap<String, Object> customize(Map<String, Object> data) {
    LinkedHashMap<String, Object> returnData = new LinkedHashMap<>();
    data.keySet().forEach(key -> {
      Object value = data.get(key);
      if (value instanceof String) {
        String stringValue = value.toString();
        if (stringValue.contains("%random%")) {
          value = stringValue.replace("%random%", RandomStringUtils.randomAlphabetic(10).toLowerCase());
        }
        if (stringValue.contains("%random5%")) {
          value = stringValue.replace("%random5%", RandomStringUtils.randomAlphabetic(5).toLowerCase());
        }
        returnData.put(key, value);
      } else if (value instanceof HashMap) {
        returnData.put(key, customize((HashMap) value));
      } else {
        returnData.put(key, value);
      }
    });
    return returnData;
  }

}
