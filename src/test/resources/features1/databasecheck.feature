Feature: Login feature for existing users

  Scenario: Successful login for an existing user
    Given the username "testUser" exists in the database
    When the user attempts to log in with username "testUser" and password "testPassword"
    Then the login should be successful

  Scenario: Login attempt with non-existing user
    Given the username "nonExistentUser" does not exist in the database
    When the user attempts to log in with username "nonExistentUser" and password "testPassword"
    Then the login should fail