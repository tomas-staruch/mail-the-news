package mail.the.news;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import mail.the.news.domain.EmailAddress;
import mail.the.news.domain.EmailMessage;
import mail.the.news.domain.EmailMessage.ContentType;
import mail.the.news.service.EmailAddressRepository;
import mail.the.news.service.EmailMessageRepository;

@SpringBootApplication
public class Application implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(Application.class);
	
	@Autowired
	private EmailMessageRepository msgsRepository;
	
	@Autowired
	private EmailAddressRepository addressesRepository;	

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		log.info("Hello");
		EmailAddress from = new EmailAddress("from@test.com");
		EmailAddress toA = new EmailAddress("toA@test.com");
		EmailAddress toB = new EmailAddress("toB@test.com");
		
		addressesRepository.save(from);
		addressesRepository.save(toA);
		addressesRepository.save(toB);
		
		EmailMessage e1 = new EmailMessage(from, toA, "Test subject A", "Test body A", "UTF-8", ContentType.PLAIN_TEXT);
		EmailMessage e2 = new EmailMessage(from, toB, "Test subject B", "Test body B", "UTF-8", ContentType.PLAIN_TEXT);
		
		msgsRepository.save(e1);
		msgsRepository.save(e2);
		

		// fetch all customers
		log.info("EmailMessage found with findAll():");
		log.info("-------------------------------");
		for (EmailMessage msg : msgsRepository.findAll()) {
			log.info(msg.toString());
		}
		
		log.info("Done");
	}

}
