package framework;


import framework.logging.CaptureLog;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import utils.DataConverters;
import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static io.restassured.RestAssured.*;
import static java.lang.String.format;

@Component
public class RestClient {

  private static final Logger LOGGER = CaptureLog.getLogger(RestClient.class);
  private static final String REQUEST_TEMPLATE
          = "Endpoint\t - %1$s\nMethod\t - %2$s\nRequest\t - %3$s\nHeaders\t - %4$s";
  private static final String RESPONSE_TEMPLATE = "Status code\t - %1$s\nResponse\t - %2$s\nHeaders\t - %3$s";

  private Map<String, String> requestHeaders;
  private Headers responseHeaders;
  private long responseTime;
  private int httpStatusCode;
  private String responseAsString;
  private String callLogMsg;
  private String responseMessage;

  @Value("${connection.timeout}")
  private Integer connectionTimeout;

  @PostConstruct
  private void postConstruct() {
    requestHeaders = new HashMap<String, String>() {
      {
        put("User-Agent", "TestingRegistration");
      }
    };
    given().headers(requestHeaders).request();
  }

  public RestClient get(String endpoint) {
    Response response = given()
            .headers(requestHeaders)
            .contentType(ContentType.JSON)
            .when()
            .get(endpoint)
            .thenReturn();
    saveObjectStateAndLogData(endpoint, HttpGet.METHOD_NAME, response, null);
    return this;
  }

  public RestClient post(String endpoint, Object request) {
    String requestAsString = DataConverters.createJsonFromObject(request);
    Response response = given()
            .headers(requestHeaders)
            .contentType(ContentType.JSON)
            .body(requestAsString)
            .when()
            .post(endpoint)
            .thenReturn();
    saveObjectStateAndLogData(endpoint, HttpPost.METHOD_NAME, response, requestAsString);
    return this;
  }

  public RestClient put(String endpoint, Object request) {
    String requestAsString = DataConverters.createJsonFromObject(request);
    Response response = given()
            .headers(requestHeaders)
            .contentType(ContentType.JSON)
            .body(requestAsString)
            .when()
            .put(endpoint)
            .thenReturn();
    saveObjectStateAndLogData(endpoint, HttpPut.METHOD_NAME, response, requestAsString);
    return this;
  }

  public RestClient delete(String endpoint) {
    Response response = given()
            .headers(requestHeaders)
            .contentType(ContentType.JSON)
            .when()
            .delete(endpoint)
            .thenReturn();
    saveObjectStateAndLogData(endpoint, HttpDelete.METHOD_NAME, response, null);
    return this;
  }

  public RestClient saveObjectStateAndLogData(String endpoint, String method, Response coreResponse, String request) {
    String requestString = request == null ? "{}" : request; // For GET method we pass argument 'request = null'
    String callLogMsg = String.format(REQUEST_TEMPLATE, endpoint, method, requestString, requestHeaders);
    this.callLogMsg = callLogMsg;
    LOGGER.info(callLogMsg);
    httpStatusCode = coreResponse.statusCode();
    responseAsString = coreResponse.getBody().asString();
    responseHeaders = coreResponse.getHeaders();
    responseTime = coreResponse.getTime();
    LOGGER.info("---------------- Total service response time: " + responseTime);
    assertThat(responseTime)
            .as(String.format("Service didn't respond in %s time, actual response time is %s", connectionTimeout, responseTime))
            .isLessThanOrEqualTo(connectionTimeout);
    responseMessage = String.format(RESPONSE_TEMPLATE, httpStatusCode, responseAsString, responseHeaders.asList());
    if (httpStatusCode == HttpStatus.SC_NO_CONTENT) { responseAsString = ""; }
    LOGGER.info(String.format(RESPONSE_TEMPLATE, httpStatusCode, responseAsString, responseHeaders));
    return this;
  }

  public String getResponseAsString() {
    return responseAsString;
  }

  public RestClient setRequestHeader(String key, String value) {
    requestHeaders.put(key, value);
    return this;
  }

  public RestClient removeRequestHeader(String key) {
    requestHeaders.remove(key);
    return this;
  }

  public RestClient removeAllHeaders() {
    requestHeaders.clear();
    return this;
  }

  public long getResponseTime() {
    return responseTime;
  }

  public int getHttpStatusCode() {
    return httpStatusCode;
  }

  public String getServiceCallDetails() {
    return format("%s\n%s", callLogMsg, responseMessage);
  }

}
