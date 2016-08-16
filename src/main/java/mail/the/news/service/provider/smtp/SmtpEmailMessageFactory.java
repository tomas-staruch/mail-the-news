package mail.the.news.service.provider.smtp;

import javax.mail.Session;

import mail.the.news.domain.EmailMessage;
import mail.the.news.domain.EmailTemplate.ContentType;

import mail.the.news.service.exception.EmailServiceException;

public class SmtpEmailMessageFactory {
	
	/**
	 * Build a proper type of email message which should be sent through SMTP service
	 */
	public static SmtpEmailMessage build(EmailMessage email, Session session) throws EmailServiceException {
		if(email.getContentType().equals(ContentType.HTML)) {
			return new HtmlEmailMessage(email, session);
		} else if(email.getContentType().equals(ContentType.PLAIN_TEXT)) {
			return new SimpleEmailMessage(email, session);
		} else if(email.getContentType().equals(ContentType.MULTI_PART)) {
			return new MultiPartEmailMessage(email, session);
		}
			
		throw new IllegalArgumentException("Unknown content type of email message");
	}
}
