package mail.the.news.service.provider.smtp;

import javax.mail.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mail.the.news.Application;
import mail.the.news.domain.EmailMessage;
import mail.the.news.domain.EmailMessage.Status;

import mail.the.news.service.exception.EmailServiceException;

import mail.the.news.service.provider.EmailService;
import mail.the.news.service.provider.SessionBuilder;

public final class SmtpService implements EmailService {
	
	private static final Logger log = LoggerFactory.getLogger(Application.class);
	
	private final Session session;
	
	public SmtpService(SessionBuilder<Session> builder) {
		this.session = builder.buildSession();
	}

	@Override
	public EmailMessage send(EmailMessage email) {
		String msgId = null;
		try {
			// TODO maybe worth to save the id into DB?
			msgId = SmtpEmailMessageFactory.build(email, session).send();
			
			email.setStatus(Status.SENT);
		} catch (EmailServiceException e) {
			email.setStatus(Status.FAILED);
		}
		
		log.info(String.format("Email %s sent to %s", msgId, email.getToEmail()));

		return email;
	}
}
