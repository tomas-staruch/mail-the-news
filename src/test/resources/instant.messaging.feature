Feature: An instant messaging
	Requitments: user has to be already registered 	

	Scenario: A user has to be registered in order to be able to send an email
	    Given the registered user with email "antony.tester@gmail.com"
	    And fallowing SMTP configuration host: "smtp.gmail.com" , pwd: "RaNdOm"
	    And addresses of recipients: "mike.tester@gmail.com" , "marc.tester@yahoo.com"
	    And email template with subject: "Hello my fellow testers" 
	    When he sends the email messages
	    Then the subscribers should received them