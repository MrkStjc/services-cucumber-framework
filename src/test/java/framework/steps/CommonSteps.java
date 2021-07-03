package framework.steps;

import framework.BaseClient;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.apache.http.HttpStatus.*;

public class CommonSteps {

  private BaseClient baseClient;

  @Autowired
  public CommonSteps(BaseClient baseClient) {
    this.baseClient = baseClient;
  }

  @Then("status code is 2xx")
  public void statusCode2xx() {
    assertThat(baseClient.getHttpStatusCode()).isGreaterThanOrEqualTo(SC_OK).isLessThanOrEqualTo(SC_NO_CONTENT);
  }

  @Then("status code is 4xx")
  public void statusCode4xx() {
    assertThat(baseClient.getHttpStatusCode()).isGreaterThanOrEqualTo(SC_BAD_REQUEST).isLessThanOrEqualTo(SC_FAILED_DEPENDENCY);
  }

  @Then("status code is 404")
  public void statusCode404() {
    assertThat(baseClient.getHttpStatusCode()).isEqualTo(SC_NOT_FOUND);
  }

  @Then("verify that response has message: {}")
  public void verifyResponseMessage(String message) {
    String response = baseClient.getResponseErrorString();
    assertThat(response).contains(message);
  }

}
