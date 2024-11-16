# Login.feature
Feature: Login
  As a user
  User want to login to the application
  So that User can access the account overview page

@jira(AUT-25) @Smoke
Scenario Outline: Login Successful
  Given User is on the login page
  When User enters valid credentials "<username>" and "<password>"
  And User clicks the login button
  Then User should be navigate to home page "<landingpage>"
  Then User logs out after successful login
  Examples:
      | username     					| password     	  	|	landingpage					|	
      | testing   						| testing123   			|	Accounts Overview		|

@jira(AUT-26) @Smoke 
Scenario Outline: Login with invalid UserName and Password
  Given User is on the login page
  When User enters invalid credentials "<username>" and "<password>"
  And User clicks the login button
  Then User should see an error message "<errormessage>"
  Examples:
      | username     					| password     	  |	errormessage	|
      | capstone1	   					| gEqUzem1   			|	The username and password could not be verified.|

@jira(AUT-27)  @Smoke    
Scenario Outline: Login with either UserName and Password
  Given User is on the login page
  When User enters invalid credentials "<username>" and "<password>"
  And User clicks the login button
  Then User should see an error message1 "<errormessage>"
  Examples:
      | username     					| password     	  |	errormessage	|
      | mngr597286   					| 				   			|	Please enter a username and password.|
      | 					   					| mngr597286 			|	Please enter a username and password.|
      | 					   					| 					 			|	Please enter a username and password.|
  
  