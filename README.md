# FRAMEWORK USAGE

## Running tests using Maven
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

env                   no default value    yes              Just to show how we can user different properties, use petstore value
test.suite            AllTests            no               To select which test suite will be ran, AllTests includes all tests and then it is best to use metafilter.
file.logging.level    FINE                no               To manage how much logging info we save in log file
console.logging.level FINE                no               To manage how much logging info we show in console
meta.filters          Empty string        no               Will be discussed in next section
connection.timeout    60000               no               Sets how much fmwk will wait for service response in millis
```

### Meta Filters
Every scenario and story file can have ```Meta:``` tags. Every meta tag value should start with ```@``` character.
Using ```meta.filters``` groovy boolean expression in run configuration we can conveniently filter tests we want to include
or exclude in test run see example on how it is used below:
```"-Dmeta.filters=groovy: debug && !skip"``` if we use groovy meta filter like this in maven run configuration we will 
execute all stories or scenarios that have ```Meta: @debug``` and don't have ```Meta: @skip``` tags.

There are several meta tags we need you use for marking our tests in convenient way:

```bash
Meta tag   Description

@feature   Allure will group all scenarios with same feature tag values
```

### Run test examples
For running tests it is best to create maven run configurations in your environment.
NOTE: This file is written on MacOS so be careful when copy pasting command line for special characters like quotation marks e.g. if running on Windows :)
Below are some examples of command line argument for various test executions:

```
clean test -PincludeTests -Denv=petstore
```

This will execute all tests that are present in default AllTests suite.

```
clean test -PincludeTests -Denv=petstore  "-Dmeta.filters=groovy: defect==\"ADIDAS-1703\""
```

This will execute all tests that have ```Meta: @defect ADIDAS-1703``` tag on petstore base environment property values.

```
clean test -PincludeTests -Denv=petstore  "-Dmeta.filters=groovy: feature==\"ManagingPetsInStore\""
```

This will execute all tests that have ```Meta: @feature ManagingPetsInStore``` tag on petstore base environment property values.

```
clean test -PincludeTests -Denv=petstore  "-Dmeta.filters=groovy: !skip"
```

This will execute all tests on petstore base environment that don't have ```Meta: @skip``` tag.

```
clean test -PincludeTests -Denv=petstore  "-Dmeta.filters=groovy: !skip && defect==\"ADIDAS-1703\""
```

This will execute all tests on petstore base environment that don't have ```Meta: @skip``` tag and have ADIDAS-1703 defect tag.

## Generating Allure report
First you need Allure installed on your machine - instructions https://docs.qameta.io/allure/
For presenting test run results in convenient way we are using allure reporting tool. To show results in browser type after execution in terminal:
```allure serve target/allure-result```

## Logging
In Console and in ```target/TestServices.log``` file run logs are saved / shown

# About core implementation

## Technologies used
1. Java
2. Maven
3. Spring boot - Dependency injection and IoC
4. JBoss resteasy client - Client for making actual service calls
5. JBehave - BDD layer
6. Jackson - JSON serialization and deserialization
7. Groovy - Groovy meta filters for test execution
8. Assertj - For assertions
9. Snakeyaml - Deserialization of .yaml files
10.Allure - Reporting

## Potential improvements
```
What                                                   How
Multithreading capability                              Implement spring framework Scope interface and use ThreadLocal to store all components annotated with @Scope(MyScope.NAME)
Display scenario meta filter values in Allure report   Inside JBehaveAllureReporter class example and beforeScenario methods use setLinks method on TestResult object
```