package mail.the.news;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import mail.the.news.domain.AddressBook;
import mail.the.news.domain.EmailAddress;
import mail.the.news.domain.EmailServiceConfiguration;
import mail.the.news.domain.EmailTemplate;
import mail.the.news.domain.SmtpServiceConfiguration;
import mail.the.news.domain.User;
import mail.the.news.repository.EmailAddressRepository;
import mail.the.news.repository.UserRepository;
import mail.the.news.security.AesSymmetricKeyEncrypter;
import mail.the.news.security.Encrypter;
import mail.the.news.security.HashEncrypter;

/**
 * Import a dummy user data when the application starts in order to do a simple demo
 *  
 * You can use curl to get the user data in json or xml format back, e.g.:
 * curl -i --header "Accept: application/json" --user dummy_user@not.existing.domain.com:RaNdOmPwD http://localhost:8080/user
 * 
 * Post a new user:
 * curl -i -H "Content-Type: application/json" --data "{\"email\":\"another.user@not.existing.domain.com\",\"password\":\"aNyPaSsWoRd\",\"name\":\"John The Tester\"}" http://localhost:8080/user
 * 
 * Request detail information about particular configuration:
 * curl -i --header "Accept: application/json" --user dummy_user@not.existing.domain.com:RaNdOmPwD http://localhost:8080/user/configurations/1
 * 
 * Post a new configuration:
 * curl -i -H "Content-Type: application/json" --user dummy_user@not.existing.domain.com:RaNdOmPwD --url http://localhost:8080/user/configurations --data "{\"port\":25,\"sslEnabled\":false,\"url\":\"another.smtp.server.com\",\"userName\":\"my@not.existing.domain.com\",\"password\":\"secrete_phrase\",\"type\":\"smtp\"}"
 * 
 * If SSL is enabled, use secure HTTP through port 9000:
 * curl -i -k --header "Accept: application/json" --user dummy_user@not.existing.domain.com:RaNdOmPwD https://localhost:9000/user
 */
@Component
class TestingUserLoader implements ApplicationListener<ContextRefreshedEvent> {
	private static final Logger log = LoggerFactory.getLogger(TestingUserLoader.class);
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmailAddressRepository addressesRepository;	
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		final String userPwd = "RaNdOmPwD";

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
		
		EmailServiceConfiguration configuration = null;
		try {
			Encrypter encrypter = new AesSymmetricKeyEncrypter(userPwd, new AesSymmetricKeyEncrypter.Seed());
			configuration = new SmtpServiceConfiguration("any.smtp.server.com", encrypter.encrypt("passwordToSmtpService"));
		} catch (Exception e) {
			throw new RuntimeException("An exception occured when configuration password was tried to be encrypted", e);
		}
				
		User user = new User(userEmail, "Dummy user", new HashEncrypter(passwordEncoder).encrypt(userPwd));
		user.addAddressBook(addressBook);
		user.addEmailTemplate(template);
		user.addConfiguration(configuration);
		
		userRepository.saveAndFlush(user);
	}

}
