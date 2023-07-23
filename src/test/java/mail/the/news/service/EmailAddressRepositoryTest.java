package mail.the.news.service;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import mail.the.news.domain.EmailAddress;
import mail.the.news.repository.EmailAddressRepository;

/**
 * Integration tests for {@link EmailAddressRepository}.
 * 
 * see https://spring.io/blog/2016/04/15/testing-improvements-in-spring-boot-1-4
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class EmailAddressRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
	
	@Autowired
	EmailAddressRepository repository;

	@Test
	public void shouldFindByEmail() {
		// given
		// every email address is is uniquely identified by email
		String theFirstEmail = "the.first@test.com", theSecondEmail = "the.second@test.com";
		List<EmailAddress> addresses = buildEmailAddress(theFirstEmail, theSecondEmail);
		
		// when
		persist(addresses);

		// then
		List<EmailAddress> actual = repository.findByEmail(theFirstEmail);
		assertThat(actual.size(), is(1));
		assertThat(actual.get(0).getEmail(), is(theFirstEmail));
	}
	
	@Test
	public void shouldExist() {
		// given
		String theFirstEmail = "the.first@test.com", theSecondEmail = "the.second@test.com";
		List<EmailAddress> addresses = buildEmailAddress(theFirstEmail, theSecondEmail);	
		// when
		persist(addresses);

		// then
		assertTrue(repository.existsByEmail(theFirstEmail));
		assertTrue(repository.existsByEmail(theSecondEmail));
	}
	
	
	@Test
	public void shouldntExist() {
		// given
		List<EmailAddress> addresses = buildEmailAddress("the.first@test.com", "the.second@test.com");
		
		// when
		persist(addresses);

		// then
		assertFalse(repository.existsByEmail("not.existing.email@test@com"));
	}
	
	private List<EmailAddress> buildEmailAddress(String... emails) {
		List<EmailAddress> addresses = new LinkedList<>();
		for(String email : emails)
			addresses.add(new EmailAddress(email, "Test recipient"));
		
		return addresses;
	}
	
	private <E> void persist(Collection<E> entities) {
		entities.forEach(entity -> this.entityManager.persist(entity));
	}
}
