package framework.steps;

import database.WrappedEntityManager;
import database.models.Users;
import framework.clients.RegistrationClient;
import framework.models.requests.LoginRequest;
import framework.models.responses.RegisterResponse;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.SoftAssertions;
import org.springframework.beans.factory.annotation.Autowired;
import utils.ScenarioContext;

import java.util.List;

import static utils.constants.ScenarioContextConstants.REGISTER_DATA;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DbSteps {

  private WrappedEntityManager wrappedEntityManager;
  private ScenarioContext scenarioContext;
  private RegistrationClient registrationClient;

  @Autowired
  public DbSteps(WrappedEntityManager wrappedEntityManager, RegistrationClient registrationClient, ScenarioContext context) {
    this.wrappedEntityManager = wrappedEntityManager;
    this.scenarioContext = context;
    this.registrationClient = registrationClient;
  }

  // Just utility method for cleaning up db from users generated during test execution.
  @When("all users generated by framework are deleted")
  public void deleteAllUsersFromFramework() {
    wrappedEntityManager.performNativeQuery("DELETE FROM USERS WHERE USERNAME LIKE '%-TST079'");
  }

  @Then("verify that get user details response has same data as stored in db")
  public void verifyGetUserDetailsSameDb() {
    RegisterResponse actual = registrationClient.getJsonResponse(RegisterResponse.class);
    String username = actual.getUsername();
    List<Users> users = wrappedEntityManager.findAllByFieldName("username", username, Users.class);
    assertThat(users.size()).isEqualTo(1);
    Users expected = users.get(0);
    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(actual.getFirstName()).as("First name is not correct.").isEqualTo(expected.getFirstName());
    softly.assertThat(actual.getLastName()).as("Last name is not correct.").isEqualTo(expected.getLastName());
    softly.assertThat(actual.getUsername()).as("Username name is not correct.").isEqualTo(expected.getUsername());
    softly.assertAll();
  }


  @Then("verify user with username from context is present in db table users")
  public void verifyUserWithId() {
    LoginRequest response = scenarioContext.get(REGISTER_DATA);
    String username = response.getUsername();
    List<Users> users = wrappedEntityManager.findAllByFieldName("username", username, Users.class);
    assertThat(users.size()).isEqualTo(1);
  }

  @Then("verify user with username from context is not present in db table users")
  public void verifyUserWithIdNotPresent() {
    LoginRequest response = scenarioContext.get(REGISTER_DATA);
    String username = response.getUsername();
    List<Users> users = wrappedEntityManager.findAllByFieldName("username", username, Users.class);
    assertThat(users.size()).isEqualTo(0);
  }

  @Then("verify that users table is empty")
  public void verifyUsersTableEmpty() {
    int numEntries = wrappedEntityManager.performNativeQuery("SELECT count(*) FROM USERS");
    assertThat(numEntries).isEqualTo(0);
  }

  @Then("verify that user has data in db from test data: {convertToUserData}")
  public void verifyUserDataInDb(LoginRequest request) {
    assertThat(scenarioContext.contains(REGISTER_DATA)).isTrue();
    LoginRequest expected = scenarioContext.get(REGISTER_DATA);
    String username = expected.getUsername();
    List<Users> actualUsers = wrappedEntityManager.findAllByFieldName("username", username, Users.class);
    assertThat(actualUsers.size()).isEqualTo(1);
    Users actualUser = actualUsers.get(0);
    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(actualUser.getFirstName()).as("First name is not correct.").isEqualTo(expected.getFirstName());
    softly.assertThat(actualUser.getLastName()).as("Last name is not correct.").isEqualTo(expected.getLastName());
    softly.assertThat(actualUser.getUsername()).as("Username name is not correct.").isEqualTo(expected.getUsername());
    softly.assertAll();
  }



}
