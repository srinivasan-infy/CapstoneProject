@Regression
# Deposit Fund API.feature
Feature: To verify the deposit fund API is working

Scenario Outline: Login Successful
  Given User is in the login page
  When User enters valid credentials "<username>" and "<password>"
  And User click the login button
  Then User verifies the Account Overview page is displayed
	Then User gets the account id from the Account Overview
	Then User extracts the CustomerID based on Account ID using API get request
	Then User deposits "10000.00" dollar for Customer 
	Then User click the Account Overview link 
	Then User verifies that deposit through API is updated in the Web UI
	
    Examples:
      | username     					| password     	  	|	landingpage					|
      | Aurelio_Thompson   						| pr6rhw7omi81k   			|	Accounts Overview		|