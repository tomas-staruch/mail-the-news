package mail.the.news.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import mail.the.news.domain.AddressBook;
import mail.the.news.domain.EmailAddress;
import mail.the.news.domain.EmailTemplate;
import mail.the.news.domain.SmtpServiceConfiguration;
import mail.the.news.domain.EmailTemplate.ContentType;
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
		String email = "from@test.com";
		User user = buildUser(email);
		
		// when
		this.entityManager.persist(user);

		// then
		List<User> actual = repository.findByEmail(email);
		assertThat(actual.size(), is(1));
		assertThat(actual.get(0).getEmailTemplates().size(), is(1));
		assertThat(actual.get(0).getAddressBooks().size(), is(1));
	}
	
	private User buildUser(String email) {
		AddressBook addressBook = new AddressBook("testing address book", Collections.singleton(new EmailAddress("to@test.com")));
		
		EmailTemplate template = new EmailTemplate("A subject", "A messge body", "UTF-8", ContentType.PLAIN_TEXT);
		template.setAddressBook(addressBook);
		
		User user = new User(email, "random_password");
		user.addAddressBook(addressBook);
		user.addEmailTemplate(template);
		user.addConfiguration(new SmtpServiceConfiguration("not.existing.server.com", "rAnDoMpWd"));
		
		return user;
	}
}
