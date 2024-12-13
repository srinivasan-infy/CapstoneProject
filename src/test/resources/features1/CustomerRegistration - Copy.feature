Feature: Customer Registration Process Using Faker/Json

@jira(AUT-24)
@Smoke @Regression
Scenario: User registers with random data
    Given User is on the registration pages
    When User fills in the registration form with random data
    And User submits the registration form
    Then User should see a confirmation message