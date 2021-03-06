
@UserDetails
Feature: Get user details using GET /users/{username}


  @smoke
  Scenario: Get user details of existing user

    Given I create new user with data: validUserData
    Then status code is 2xx
    When I get user details for username: from context
    Then verify that get user details response has same data as stored in db


  @smoke @negative
  Scenario: Bad request error on get user details of non existing user attempt

    When I get user details for username: notExistingUser123
    Then status code is 404