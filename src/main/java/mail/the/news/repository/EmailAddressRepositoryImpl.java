package mail.the.news.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import mail.the.news.domain.EmailAddress;

/**
 * Implementation of useful custom repository methods
 */
public class EmailAddressRepositoryImpl implements EmailAddressRepositoryCustom {
	
	@Autowired
    private EmailAddressRepository repository;
    
	/**
	 * {@link EmailAddress}s are shared to prevent duplications that's why method try to find such email
	 * before it is created
	 * 
	 * TODO: that's not good approach because EmailAddress.name can differ from user to user
	 */
	@Override
	public EmailAddress findOrCreate(EmailAddress entity) {
		// there is unique constraint on email, so only one EmailAddress can be found
		List<EmailAddress> addresses = repository.findByEmail(entity.getEmail());
		if(addresses.size() > 0) 
			return addresses.get(0);
		
		return repository.save(entity);
	}

}
