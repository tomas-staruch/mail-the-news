package mail.the.news.service.provider.smtp;

import java.util.Collection;

import javax.mail.Session;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import mail.the.news.domain.EmailMessage;
import mail.the.news.service.exception.EmailServiceException;

class SimpleEmailMessage extends SmtpEmailMessage {

	public SimpleEmailMessage(EmailMessage emailMessage, Session session) throws EmailServiceException {	
		super(new SimpleEmail(), emailMessage, session);
	}

	@Override
	protected void setBody(String body) throws EmailException {
		email.setMsg(body);
	}
	
	@Override	
	protected void attach(Collection<String> filesToAttach) throws EmailException {
    	throw new UnsupportedOperationException("Files cannot be attached to SimpleEmail");
    }
}
