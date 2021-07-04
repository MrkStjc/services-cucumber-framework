package framework.models.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseWrapper<T> {

  private T body;
  private JsonNode rawResponse;
  private long responseTime;
  private int statusCode;
  private String errorMessage;

  public void setBody(T body) {
    this.body = body;
  }

  public void setResponseTime(long responseTime) {
    this.responseTime = responseTime;
  }

  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  public void setRawResponse(JsonNode rawResponse) {
    this.rawResponse = rawResponse;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

}
