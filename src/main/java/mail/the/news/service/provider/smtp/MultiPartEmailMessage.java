package mail.the.news.service.provider.smtp;

import java.util.Collection;

import javax.mail.Session;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

import mail.the.news.domain.EmailMessage;
import mail.the.news.service.exception.EmailServiceException;

class MultiPartEmailMessage extends SmtpEmailMessage {

	public MultiPartEmailMessage(EmailMessage emailMessage, Session session) throws EmailServiceException {
		super(new MultiPartEmail(), emailMessage, session);
	}

	@Override
	protected void setBody(String body) throws EmailException {
		email.setMsg(body);
	}
	
	@Override	
	protected void attach(Collection<String> filesToAttach) throws EmailException {
    	for(String file : filesToAttach)
    		((MultiPartEmail)this.email).attach(createAttachment(file));
    }
}
