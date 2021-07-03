package framework.steps;

import framework.clients.RegistrationClient;
import framework.models.requests.LoginRequest;
import framework.models.responses.RegisterResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.SoftAssertions;
import org.springframework.beans.factory.annotation.Autowired;
import utils.ScenarioContext;
import utils.enums.LoginFields;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static utils.constants.ScenarioContextConstants.REGISTER_DATA;
import static utils.constants.ScenarioContextConstants.USERNAME;

public class UserServiceSteps {

  private ScenarioContext scenarioContext;

  private RegistrationClient registrationClient;

  @Autowired
  public UserServiceSteps(ScenarioContext scenarioContext, RegistrationClient registrationClient) {
    this.registrationClient = registrationClient;
    this.scenarioContext = scenarioContext;
  }

  @Given("I create new user with data: {convertToUserData}")
  public void createNewUser(LoginRequest request) {
    scenarioContext.put(REGISTER_DATA, request);
    scenarioContext.put(USERNAME, request.getUsername());
    registrationClient.register(request);
  }

  @When("I create new user with data from context")
  public void createUserWithDataFromContext() {
    assertThat(scenarioContext.contains(REGISTER_DATA)).isTrue();
    LoginRequest request = scenarioContext.get(REGISTER_DATA);
    registrationClient.register(request);
  }

  @When("I login using login data from context")
  public void loginWithDataFromContext() {
    assertThat(scenarioContext.contains(REGISTER_DATA)).isTrue();
    LoginRequest request = scenarioContext.get(REGISTER_DATA);
    registrationClient.login(request);
  }

  @When("I login using login data from context and {} field value: {}")
  public void loginWithDataFromContextWithFieldValue(LoginFields fieldName, String value) {
    assertThat(scenarioContext.contains(REGISTER_DATA)).isTrue();
    LoginRequest request = scenarioContext.get(REGISTER_DATA);
    switch (fieldName) {
      case PASSWORD:
        request.setPassword(value);
        break;
      case USERNAME:
        request.setUsername(value);
        break;
      case LAST_NAME:
        request.setLastName(value);
        break;
      case FIRST_NAME:
        request.setFirstName(value);
        break;
      default: throw new IllegalArgumentException("Invalid field name used!");
    }
    registrationClient.login(request);
  }

  @When("I get user details for username: {}")
  public void getUserDetails(String userName) {
    LoginRequest data;
    String username;
    if ("from context".equalsIgnoreCase(userName)) {
      assertThat(scenarioContext.contains(REGISTER_DATA)).isTrue();
      data = scenarioContext.get(REGISTER_DATA);
      username = data.getUsername();
    } else {
      username = userName;
    }
    registrationClient.findUserByUsername(username);
  }

  @When("I delete user with username: {}")
  public void deleteUser(String userName) {
    LoginRequest data;
    String username;
    if ("from context".equalsIgnoreCase(userName)) {
      assertThat(scenarioContext.contains(REGISTER_DATA)).isTrue();
      data = scenarioContext.get(REGISTER_DATA);
      username = data.getUsername();
    } else {
      username = userName;
    }
    registrationClient.deleteUserByUsername(username);
  }

  @When("I delete all users")
  public void deleteAllUsers() {
    registrationClient.deleteAllUsers();
  }

  @Then("verify that user details response has proper data from context")
  public void verifyRegisterUserResponse() {
    assertThat(scenarioContext.contains(REGISTER_DATA)).isTrue();
    LoginRequest expected = scenarioContext.get(REGISTER_DATA);
    RegisterResponse actual = registrationClient.getJsonResponse(RegisterResponse.class);
    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(actual.getFirstName()).as("First name is not correct.").isEqualTo(expected.getFirstName());
    softly.assertThat(actual.getLastName()).as("Last name is not correct.").isEqualTo(expected.getLastName());
    softly.assertThat(actual.getUsername()).as("Username name is not correct.").isEqualTo(expected.getUsername());
    softly.assertAll();
  }

  @Then("verify register user response has username from context")
  public void verifyRegisterUserUsername() {
    assertThat(scenarioContext.contains(REGISTER_DATA)).isTrue();
    LoginRequest expected = scenarioContext.get(REGISTER_DATA);
    RegisterResponse actual = registrationClient.getJsonResponse(RegisterResponse.class);
    assertThat(actual.getUsername()).as("Username name is not correct.").isEqualTo(expected.getUsername());
  }

}
