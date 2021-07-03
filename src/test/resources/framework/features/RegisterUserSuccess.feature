
@RegisterUserSuccess
Feature: Success on new user registration using PUT /users/register API with valid data.


  @Issue1 @smoke
  Scenario: Register new user with unique username and verify response data

    Given I create new user with data: validUserData
    Then status code is 2xx
    And verify that user details response has proper data from context


  @Issue1 @smoke
  Scenario: Register new user and verify it is properly stored in database
    Given I create new user with data: validUserData
    Then status code is 2xx
    And verify that user has data in db from test data: validUserData


  @Issue2
  Scenario: Success on valid boundary firstName value

    Given I create new user with data: firstName1Char
    Then status code is 2xx


  @Issue2
  Scenario: Success on valid boundary lastName value

    Given I create new user with data: lastName1Char
    Then status code is 2xx


  @Issue5
  Scenario Outline: Success on valid input values for username

    Given I create new user with data: <data>
    Then status code is 2xx
    And verify register user response has username from context

    Examples:
    |data          |
    |username6Char |
    |username7Char |
    |username49Char|
    |username50Char|





