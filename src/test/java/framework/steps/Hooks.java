package framework.steps;

import framework.BaseClient;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import org.springframework.beans.factory.annotation.Autowired;
import utils.ScenarioContext;

import static io.restassured.RestAssured.*;

public class Hooks {

  @Autowired
  private ScenarioContext scenarioContext;

  @Autowired
  private BaseClient baseClient;

  @After
  public void afterScenario(Scenario scenario) {
    if (scenario.isFailed()) {
      scenario.attach(baseClient.getAllureAttachment(), "text/plain", scenario.getName());
    }
    baseClient.removeAllHeaders();
    scenarioContext.clear();
    reset();
  }

}
