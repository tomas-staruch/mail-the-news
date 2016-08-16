Feature: A user can define a schedule for a template to be sent
	    
	Scenario: A user chose template which should be regullary resent
	    Given the registered user with email "antony.tester@gmail.com"
	    And fallowing SMTP configuration host: "smtp.gmail.com" , pwd: "RaNdOm"
	    And address of recipient: "info@dummy.supplier.com"
	    And email template with subject: "Please send me actuall price list" 
	    And schedule for template to be sent every monday at 9am
	    When the email is sent
	    Then the subscribers should receive it