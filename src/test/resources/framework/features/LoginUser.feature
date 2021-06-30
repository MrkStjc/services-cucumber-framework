
@LoginUser
Feature: Login user using POST /users/register API.


  Scenario: Success on login attempt for new user

    Given I create new user with data: validUserData
    Then status code is 2xx
    When I login using login data from context
    Then status code is 2xx


  Scenario Outline: Unable to login with non existing username or password

    Given I create new user with data: validUserData
    Then status code is 2xx
    When I login using login data from context and <fieldName> field value: <value>
    Then status code is 4xx
    Then verify that response has message: Login failed.

    Examples:
    |fieldName |value                |
    |USERNAME  |nonexistinguser345444|
    |PASSWORD  |Marko12345!!         |
    |FIRST_NAME|IDontExistFN         |
    |LAST_NAME |IDontExistLN         |


  Scenario: Unable to login using existing user username and other user password

    Given I create new user with data: validUserData
    Then status code is 2xx
    Given I create new user with data: validUserDataOtherPassword
    When I login using login data from context and PASSWORD field value: Test1234!
    Then status code is 4xx
    Then verify that response has message: Login failed.

