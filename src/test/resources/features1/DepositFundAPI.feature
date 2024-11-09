@Regression
# Deposit Fund API.feature
Feature: To verify the deposit fund API is working

Scenario Outline: Deposit Fund API verification
  Given User is on the login page
  When User enters valid credentials "<username>" and "<password>"
  And User clicks the login button
  Then User verifies the Account Overview page is displayed
	Then User gets the account id from the Account Overview
	Then User extracts the CustomerID based on Account ID using API get request
	Then User deposits "10000.00" dollar for Customer 
	Then User click the Account Overview link 
	Then User verifies that deposit through API is updated in the Web UI
	Then User logs out after successful login
	
    Examples:
      | username     					| password     	  	|	landingpage					|
      | capstone   						| testing123   			|	Accounts Overview		|