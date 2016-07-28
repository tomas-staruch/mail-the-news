Feature: Send an email

  Scenario: Application should send an invitation email
    Given text message "Warm greetings to my new followers"
    And and there are "antony@gmail.com" and "marc@yahoo.com" who subsribed it
    When I send the email message
    Then the subscribers should received it
    
  Scenario: Application should send a personalized email
    Given template message "Hello %name%, you are welcome at my party!"
    And "Antony" with address "antony@gmail.com" 
	And "Marc" with address "marc@yahoo.com"
    When I send the email message
    Then the they should receive it