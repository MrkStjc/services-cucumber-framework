
@RegisterUserNegative
Feature: Bad request and error on new user registration using PUT /users/register API with invalid data.


  @smoke
  Scenario: Bad request Error on registration attempt of new user with same data as existing one

    Given I create new user with data: validUserData
    Then status code is 2xx
    When I create new user with data from context
    Then status code is 4xx
    And verify that response has message: User with the specified username already exists!


  @Issue3 @negative
  Scenario Outline: Bad request and proper error on invalid input values for firstName

    Given I create new user with data: <data>
    Then status code is 4xx
    And verify that response has message: <error>

    Examples:
      |data                 |error                                    |
      |firstName15Char      |First Name should be less than 15        |
      |firstName16Char      |First Name should be less than 15        |
      |firstName32Char      |First Name should be less than 15        |
      |firstNameEmpty       |First Name should have more than one char|


  @Issue3 @negative
  Scenario Outline: Bad request and proper error on invalid input values for lastName

    Given I create new user with data: <data>
    Then status code is 4xx
    And verify that response has message: <error>

    Examples:
      |data                |error                                   |
      |lastName15Char      |Last Name should be less than 15        |
      |lastName16Char      |Last Name should be less than 15        |
      |lastName32Char      |Last Name should be less than 15        |
      |lastNameEmpty       |Last Name should have more than one char|


  @Issue4 @negative
  Scenario Outline: Bad request Error on invalid input values for password

    Given I create new user with data: <data>
    Then status code is 4xx
    And verify that response has message: <error>

    Examples:
      |data                |error                                                                                      |
      |passwordNoCapital   |Password should contain combination of capital and small letters, special chars and numbers|
      |passwordNoSmall     |Password should contain combination of capital and small letters, special chars and numbers|
      |passwordNoNumber    |Password should contain combination of capital and small letters, special chars and numbers|
      |passwordNoSpecial   |Password should contain combination of capital and small letters, special chars and numbers|
      |password40Char      |Password should be less than 30                                                            |
      |passwordEmpty       |Password should contain combination of capital and small letters, special chars and numbers|


  @Issue5 @negative
  Scenario Outline: Bad request Error on invalid input values for username

    Given I create new user with data: <data>
    Then status code is 4xx
    And verify that response has message: <error>

    Examples:
      |data          |error                                                                       |
      |username5Char |Username must not be longer than 50 characters or shorter than 6 characters.|
      |username51Char|Username must not be longer than 50 characters or shorter than 6 characters.|