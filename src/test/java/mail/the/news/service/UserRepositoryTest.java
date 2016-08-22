package mail.the.news.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import mail.the.news.domain.AddressBook;
import mail.the.news.domain.EmailAddress;
import mail.the.news.domain.EmailTemplate;
import mail.the.news.domain.EmailTemplate.ContentType;
import mail.the.news.domain.SmtpServiceConfiguration;
import mail.the.news.domain.User;
/**
 * Integration tests for {@link UserRepository}.
 * 
 * see https://spring.io/blog/2016/04/15/testing-improvements-in-spring-boot-1-4
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
	
	@Autowired
	UserRepository repository;

	@Test
	public void shouldFindByEmail() {
		// given
		// every user is uniquely identified by email
		String userEmail = "from@test.com";
		EmailAddress recipient = new EmailAddress("to@test.com");
		User user = buildUser(userEmail, recipient);
		
		// when
		this.entityManager.persistAndFlush(recipient);
		this.entityManager.persistAndFlush(user);

		// then
		User actual = repository.findByEmail(userEmail);
		assertThat(actual.getEmailTemplates().size(), is(1));
		assertThat(actual.getAddressBooks().size(), is(1));
	}
	
	private User buildUser(String userEmail, EmailAddress recipient) {
		AddressBook addressBook = new AddressBook("testing address book", Stream.of(recipient).collect(Collectors.toSet()));
		
		EmailTemplate template = new EmailTemplate("A subject", "A messge body", "UTF-8", ContentType.PLAIN_TEXT);
		template.setAddressBook(addressBook);
		
		User user = new User(userEmail, "eNcOded");
		user.addAddressBook(addressBook);
		user.addEmailTemplate(template);
		user.addConfiguration(new SmtpServiceConfiguration("not.existing.server.com", "rAnDoMpWd"));
		
		return user;
	}
}
