package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.List;

public class DataConverters {

  private static final ObjectMapper mapper = new ObjectMapper();

  public static String createJsonFromObject(Object object) {
    try {
      return mapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      System.out.println(e.getMessage() + "\n");  // TODO: Implement log4j logs
      System.out.println(e.getStackTrace() + "\n");
      return "";
    }
  }

  public static <T> T convertJsonStringToObject(String responseAsString, Class<T> clazz) {
    T response;
    if (responseAsString.isEmpty()) { responseAsString = "{}"; }
    try {
      response = mapper.readValue(mapper.readTree(responseAsString).toString(), clazz);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return response;
  }

  public static <T> List<T> convertJsonStringToListObjects(String responseAsString, Class<T> clazz) {
    List<T> resultDto;
    CollectionType typeReference =
            TypeFactory.defaultInstance().constructCollectionType(List.class, clazz);
    if (responseAsString.isEmpty()) { responseAsString = "{}"; }
    try {
      resultDto = mapper.readValue(mapper.readTree(responseAsString).toString(), typeReference);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return resultDto;
  }

}
