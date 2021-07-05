# FRAMEWORK USAGE

## Introduction
Defects.md file contains defects found in application under test.
Here we have full API testing framework that is scalable and configurable enough for commercial usage.
With it I wanted to demonstrate one of the ways one API testing framework can look like. Purpose is to wrap technologies used 
so it can allow QA engineers to write tests in convenient way following pattern without having to know much about technical details behind - 
e.g. doesn't need to be proficient in Spring - just autowire stuff it needs...

We can in this framework find example of integration - functional approach to testing an APIs. We could use also plugins / dependencies that import
swagger.json file and we can then validate API schemas and so on, but that might be more scope of related API unit tests.

## Running tests using Maven
Use Java 11 in your dev environment.
You should navigate to this project root directory and open terminal at this folder - IDEA always opens 
terminal at project root folder. Starting command is ```mvn``` then ```test``` following options with which you
want to run your tests. In next sections we will mention all mandatory and optional run options.
It is best to create new maven configurations in IDEA itself (Or any other dev env you are using) and choose it as needed.

### Maven profiles
In this testing framework we have only one main profile which is mandatory if we want to start test execution.
Add ```-PincludeTests``` to your maven run configuration. All other options are managed through run config variables 
that will be explained in next section.

### Variables
Following variables are available:
```bash
Variable              Default value       Is mandatory     Description

env                   no default value    yes              Just to show how we can user different properties, use local value for testing local instance of app
db.key                no default value    yes              Key for db connection password decription - Use keyForDbPasswordEncryption value.
cucumber.filter.tags  Empty string        no               For filtering tests to run according to scenario / feature tags
connection.timeout    60000               no               Sets how much fmwk will wait for service response in millis
```

### Cucumber filter tags
Every scenario and feature file can have tags defined. Every tag value should start with ```@``` character.
Using ```cucumber.filter.tags``` cucumber expression in run configuration we can conveniently filter tests we want to include
or exclude in test run, see example on how it is used below:
```-Dcucumber.filter.tags="@debug and not @skip"``` if we use cucumber filter like this in maven run configuration we will 
execute all features or scenarios that have ```@debug``` and don't have ```@skip``` tags.


### Run test examples
For running tests it is best to create maven run configurations in your environment.
NOTE: This file is written on MacOS so be careful when copy pasting command line for special characters like quotation marks e.g. if running on Windows :)
Below are some examples of command line argument for various test executions:

```
clean test -PincludeTests -Denv=local -Ddb.key=keyForDbPasswordEncryption
```

This will execute all tests that are present in cucumber glue path - in resources.framework.features path.

```
clean test -PincludeTests -Denv=local  -Dcucumber.filter.tags="@debug and not @skip" -Ddb.key=keyForDbPasswordEncryption
```

This will execute all tests that have ```@debug``` and not ```@skip``` tag on local base environment property values.

```
clean test -PincludeTests -Denv=local  -Dcucumber.filter.tags=@RegisterUser -Dconnection.timeout=10000
```

This will execute all tests that have ```@RegisterUser``` tag on local base environment property values with response wait time of 10s.


## Generating Allure report
First you need Allure installed on your machine - instructions https://docs.qameta.io/allure/
For presenting test run results in convenient way we are using allure reporting tool. To show results in browser type after execution in terminal:
```allure serve target/allure-results```

Also more convenient way of generating allure results is using allure_report.sh shell script. Just type ```sh allure-report.sh```.


## View Cucumber HTML report
It is recommended to user allure reports for results view cause it is very detailed and pretty report. If for some reason
you cannot install allure on your machine, you can rely on cucumber html reports stored at ```target/cucumber-reports/index.html```

## Report attachment on failed test
When test fails you can find attached request / response log in allure / cucumber html report. 
For allure report simply open failed test and expand teardown section.

## Logging
In Console and in ```target/TestServices.log``` file run logs are saved / shown

# About core implementation

## Technologies used
1. Java
2. Maven
3. Spring boot - Dependency injection and IoC
4. spring-orm - For establishing database connection
4. Rest assured client - Client for making actual service calls
5. Cucumber - BDD layer
6. Jackson - JSON serialization and deserialization
7. Assertj - For assertions
8. Snakeyaml - Deserialization of .yaml files
9. Allure - Reporting

## Potential improvements
```
What                                                                                   How
Multithreading capability                                                              Implement spring framework Scope interface and use ThreadLocal to store all components annotated with @Scope(MyScope.NAME).
Several database connections using spring orm                                          Implementation is similar to current one just since we would be creating beans with same return types we would need to define @Qualifier annotations for such beans.
Allure attachment below test steps - not in teardown                                   It would require custom allure lifecycle implementation - therefore we wouldn't attach screenshot in @After in Hooks.class
Run application under test in local env before tests and close it after tests are done Create maven profile and inject script that will build app - one of parameters provided can be path to .war file on local machine. 
Remove annoying spring boot INFO and DEBUG logs from console                           Identify dependencies that are generating these logs and use log4j.xml file to manage them
```