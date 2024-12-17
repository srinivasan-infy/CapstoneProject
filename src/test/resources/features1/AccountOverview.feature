# Open New Account.feature
Feature: Account Overview Page verification test


@Regression
Scenario Outline: Account Overview Page verification
  Given User is on the login page
  When User enters valid credentials "<username>" and "<password>"
  And User clicks the login button
  Then User verifies the Account Overview page is displayed
	Then User verifies the table header is present
	Then User verifies Balance includes deposits text is displayed  
	Then User verifies the total balance is correct
  Then User logs out after successful login
  Examples:
      | username     				| password   	  	|	landingpage					|
      | testing11   				| testing123   		|	Accounts Overview		|