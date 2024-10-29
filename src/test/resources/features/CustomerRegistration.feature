Feature: Customer Registration Process Using Faker/Json
#to test the development branch
Scenario: User registers with random data
    Given User is on the registration pages
    When User fills in the registration form with random data
    And User submits the registration form
    Then save registration data to JSON
    Then User should see a confirmation message