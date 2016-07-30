package mail.the.news.service;

import org.springframework.data.repository.CrudRepository;

import mail.the.news.domain.EmailAddress;

public interface EmailAddressRepository extends CrudRepository<EmailAddress, Long> {

}
