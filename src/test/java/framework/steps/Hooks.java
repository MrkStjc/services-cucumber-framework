package framework.steps;

import framework.BaseClient;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import org.springframework.beans.factory.annotation.Autowired;
import utils.ScenarioContext;

import static io.restassured.RestAssured.*;

public class Hooks {

  private ScenarioContext scenarioContext;

  private BaseClient baseClient;

  @Autowired
  public Hooks(ScenarioContext scenarioContext, BaseClient baseClient) {
    this.scenarioContext = scenarioContext;
    this.baseClient = baseClient;
  }

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
