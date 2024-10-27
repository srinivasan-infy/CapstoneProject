Feature: User Registration Process with Valid and Invalid Scenario

  Scenario: Register multiple users from Excel
    Given User is on home page
    When Verify user is on the registration page {"Signing up is easy!"}
   	When User enters registration details from excel file "./DataSheet/RegistrationData.xlsx"

   
    