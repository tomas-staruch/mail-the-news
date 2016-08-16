package mail.the.news.service.provider;

import mail.the.news.domain.EmailMessage;

/**
 * The service which process email and send it to network
 */
public interface EmailService {
	
	public EmailMessage send(EmailMessage email);
}
