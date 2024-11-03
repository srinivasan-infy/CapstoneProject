Feature: Update the Customer Information Details


Scenario: User updates the Customer Information Details
    Given User is on home page
    When Verify user is on the registration page {"Signing up is easy!"}
    When User enters registration details from excel file "./DataSheet/RegDataUpdateCustomer.xlsx"


    Then Logout after successful login
    And User enter the credential from excel sheet
    Then User click on the Update Customer link
    Then User update the customer info from excel sheet
    And Click on the Submit button
    Then User verify the customer info is updated.