Feature: Multiple Customer Registration Process Using Faker/Json

Scenario: User registers with random data
    Given User is on the registration pages
    When User fills in the registration form with random data for 3 users
    And save all successfully registered users data to a single JSON file
