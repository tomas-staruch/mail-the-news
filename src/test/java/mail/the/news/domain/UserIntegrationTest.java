package mail.the.news.domain;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import mail.the.news.domain.EmailMessage.Status;
import mail.the.news.security.AesSymmetricKeyEncrypter;
import mail.the.news.security.Encrypter;
import mail.the.news.security.HashEncrypter;
import mail.the.news.service.EmailService;

@RunWith(SpringRunner.class)
@SpringBootTest //TODO without JPA ?
public class UserIntegrationTest {
	// the email is unique identifier of user
	private final String userEmail = "random@any.domain.com";
	private final List<String> templateIdentificators = Arrays.asList("A", "B");
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * Dummy service which only change status from CREATED to SENT
	 */
	private final EmailService testService = new EmailService() {
		@Override
		public EmailMessage send(EmailMessage email) {
			if(email == null) {
				fail("Given email message is null");
			}
			
			email.setStatus(Status.SENT);
			
			return email;
		}
	};
	
	private User user;
	
	@Before
	public void setup() throws Exception {
		final String userPwd = "random_password";

		// init the user with some data
		Set<EmailAddress> recipients = Arrays.stream(
				new EmailAddress[] { 
						new EmailAddress("a@any.domain.com"), 
						new EmailAddress("b@any.domain.com") 
				}).collect(Collectors.toSet());
		
		AddressBook addressBook = new AddressBook("testing address book", recipients);
		
		Encrypter encrypter = new AesSymmetricKeyEncrypter(userPwd, new AesSymmetricKeyEncrypter.Seed());

		this.user = new User(userEmail, new HashEncrypter(passwordEncoder).encrypt(userPwd));
		this.user.addAddressBook(addressBook);
		this.user.addConfiguration(new SmtpServiceConfiguration("not.existing.smtp.server.com", encrypter.encrypt("smtpAccountPassword")));	
		
		this.templateIdentificators.forEach(identificator -> {
			EmailTemplate template = new EmailTemplate(String.format("%s", identificator), "Messge body");
			template.setAddressBook(addressBook);
			
			this.user.addEmailTemplate(template);
		});
	}

	@Test
	public void shouldSendEmail() {
		// given
		EmailTemplate template = findTemplateBySubject(user.getEmailTemplates(), templateIdentificators.get(0));
		
		// when
		Set<EmailMessage> emails = user.composeMessages(template, "First round").sendAll(testService);

		// then
		assertThat(emails.size(), is(2));
		assertTrue(allMatch(emails, Status.SENT)); // the status of every email should be changed
	}
	
	private EmailTemplate findTemplateBySubject(Set<EmailTemplate> templates, String subjectToFind) {
		return templates.stream().filter(template -> template.getSubject().equals(subjectToFind)).findFirst().get();
	}

	private boolean allMatch(Set<EmailMessage> emails, Status expected) {
		return emails.stream().allMatch(new Predicate<EmailMessage>() {
			@Override
			public boolean test(EmailMessage msg) {
				return msg.getStatus().equals(expected);
			} 
		}
	);
	}
}
