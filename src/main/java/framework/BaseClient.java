package framework;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import framework.logging.CaptureLog;
import framework.models.responses.ResponseWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import utils.DataConverters;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@Component
public class BaseClient {

  @Value("${base.url}")
  protected String baseUrl;

  @Autowired
  protected RestClient restClient;

  @Autowired
  protected ObjectMapper objectMapper;

  private static final Logger LOGGER = CaptureLog.getLogger(BaseClient.class);

  public <T> T getJsonResponse(Class<T> clazz) {
    return DataConverters.convertJsonStringToObject(restClient.getResponseAsString(), clazz);
  }

  public <T> List<T> getJsonListResponse(Class<T> clazz) {
    return DataConverters.convertJsonStringToListObjects(restClient.getResponseAsString(), clazz);
  }

  public String getResponseErrorString() {
    return restClient.getResponseAsString();
  }

  public <T> ResponseWrapper<T> getWrappedResponseObject(Class<T> clazz) {
    ResponseWrapper<T> responseWrapper = new ResponseWrapper<>();
    responseWrapper.setResponseTime(restClient.getResponseTime());
    responseWrapper.setStatusCode(restClient.getHttpStatusCode());
    if (StringUtils.isBlank(restClient.getResponseAsString())) {
      return responseWrapper;
    }
    try {
      JsonNode tree = objectMapper.readTree(restClient.getResponseAsString());
      responseWrapper.setRawResponse(tree);
      responseWrapper.setBody(readValue(tree, clazz));
      responseWrapper.setErrorMessage(readValue(tree, String.class));
    } catch (IOException e) {
      LOGGER.warning("Response doesn't contain valid JSON.");
    }
    return responseWrapper;
  }

  private <T> T readValue(JsonNode tree, Class<T> clazz) {
    try {
      return objectMapper.readValue(tree.toString(), clazz);
    } catch (IOException e) {
      LOGGER.warning("Unable to parse part of response, it is expected.");
      return null;
    }
  }

  public void setRequestHeader(String key, String value) {
    restClient.setRequestHeader(key, value);
  }

  public int getHttpStatusCode() {
    return restClient.getHttpStatusCode();
  }

  public void removeAllHeaders() {
    restClient.removeAllHeaders();
  }

  public String getAllureAttachment() {
    return restClient.getServiceCallDetails();
  }

}