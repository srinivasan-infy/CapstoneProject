# Open New Account.feature
Feature: Open New Account verification

Scenario Outline: Login Successful
  Given User is in the login page
  When User enters valid credentials "<username>" and "<password>"
  And User click the login button
  Then User verifies the Account Overview page is displayed
	Then User verifies the table header is present
	#Then User verifies account details are valid
	Then User verifies Balance includes deposits text is displayed  
	Then User verifies the total balance is correct
  Then Logout after successful login
  Examples:
      | username     				| password   	  	|	landingpage					|
      | testing11   				| testing123   		|	Accounts Overview		|