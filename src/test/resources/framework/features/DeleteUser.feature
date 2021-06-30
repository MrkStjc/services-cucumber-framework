# new feature
# Tags: optional

Feature: Delete user and all users using DELETE /users/{username} or /users/deleteAllUsers


  Scenario: Delete newly created user
    Given I create new user with data: validUserData
    Then status code is 2xx
    When I delete user with username: from context
    Then status code is 2xx
    Then verify that response has message: is removed
    When I get user details for username: from context
    Then status code is 404

  Scenario Outline: Error on deleting non existing user attempt

    Given I create new user with data: validUserData
    Then status code is 2xx
    When I delete user with username: <username>
    Then verify that response has message: User doesnt exist!

    Examples:
    |username          |
    |null              |
    |nonExistingUser123|

    @debug
  Scenario: Delete all users

    When I delete all users