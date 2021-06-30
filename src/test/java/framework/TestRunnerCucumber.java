package framework;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(monochrome = true,
        plugin = {"pretty",
                "json:target/cucumber-reports/Cucumber.json",
                "io.qameta.allure.cucumber6jvm.AllureCucumber6Jvm",
                "html:target/cucumber-reports/index.html"})
public class TestRunnerCucumber extends AbstractTestNGCucumberTests {}