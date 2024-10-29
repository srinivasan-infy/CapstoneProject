# Account Overview.feature
Feature: Account Overview Page verification


Scenario Outline: Login Successful
  Given User is in the login page
  When User enters valid credentials "<username>" and "<password>"
  And User click the login button
  Then User verifies the Account Overview page is displayed
	Then User fetch the account data into excel
	Then User clicks on the Open New Account link
	Then User select the account type "SAVINGS"
	Then User fetch the Minimum deposit amount details
	Then User choose the account and click on Open New Account
	Then User verify account opened status
	And User click the Account Overview link
	Then User verifies the created new account and the deduction from the other account
  Then Logout after successful login
  Examples:
      | username     					| password     	  	|	landingpage					|
      | testing11   						| testing123   			|	Accounts Overview		|