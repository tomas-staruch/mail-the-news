package mail.the.news;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import mail.the.news.domain.AddressBook;
import mail.the.news.domain.EmailAddress;
import mail.the.news.domain.EmailTemplate;
import mail.the.news.domain.User;
import mail.the.news.service.EmailAddressRepository;
import mail.the.news.service.UserRepository;

/**
 * Import a dummy user data when the application starts in order to do a simple demo
 *  
 * You can use curl to get the user data in json or xml format back:
 * curl -k --header "Accept: application/json" --user dummy_user@not.existing.domain.com:RaNdOmPwD http://localhost:8080/user
 * 
 * if SSL enabled:
 * curl -k --header "Accept: application/json" --user dummy_user@not.existing.domain.com:RaNdOmPwD https://localhost:9000/user
 */
@Component
class TestingUserLoader implements ApplicationListener<ContextRefreshedEvent> {
	private static final Logger log = LoggerFactory.getLogger(TestingUserLoader.class);
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmailAddressRepository addressesRepository;	
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		log.info("Loading of dummy data started ...");
		
		String userEmail = "dummy_user@not.existing.domain.com";
		if(addressesRepository.existsByEmail(userEmail)) 
			return ;
		
		EmailAddress toA = addressesRepository.findOrCreate(new EmailAddress("user_a@not.existing.domain.com"));
		EmailAddress toB = addressesRepository.findOrCreate(new EmailAddress("user_b@not.existing.domain.com"));
		EmailAddress toC = addressesRepository.findOrCreate(new EmailAddress("user_c@not.existing.domain.com"));
		
		AddressBook addressBook = new AddressBook("Address book of not existing recipients", Stream.of(toA, toB, toC).collect(Collectors.toSet()));
	
		EmailTemplate template = new EmailTemplate("Welcome to the first issue of newsletter", "Thanks for signing up to keep in touch. From now on, you'll get regular updates.");
		template.setAddressBook(addressBook);
		
		User user = new User(userEmail, "Dummy user", new BCryptPasswordEncoder().encode("RaNdOmPwD"));
		user.addAddressBook(addressBook);
		user.addEmailTemplate(template);
		
		userRepository.saveAndFlush(user);
		
	}

}
