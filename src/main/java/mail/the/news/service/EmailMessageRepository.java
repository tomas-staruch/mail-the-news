package mail.the.news.service;

import org.springframework.data.repository.CrudRepository;

import mail.the.news.domain.EmailMessage;

public interface EmailMessageRepository extends CrudRepository<EmailMessage, Long> {

}
