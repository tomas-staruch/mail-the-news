package mail.the.news.service.provider.smtp;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Pattern;

import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;

import mail.the.news.domain.EmailMessage;
import mail.the.news.service.exception.EmailServiceException;

public abstract class SmtpEmailMessage {
	private static final String EMPTY_STR = "";
	
    private static final Pattern WHITE_SPACES_PATTERN = Pattern.compile("\\s+");
    private static final Pattern CONTROL_CHARACTERS_PATTERN = Pattern.compile("[\u0000-\u001f]");
    
    protected final Email email;
	
	public SmtpEmailMessage(Email email, EmailMessage emailMessage, Session session) throws EmailServiceException {
		this.email = email;
		
		try {
			email.setMailSession(session);
			email.setCharset(emailMessage.getEncoding());
			email.setTo(Collections.singleton(toInternetAddress(emailMessage.getToEmail())));
			email.setFrom(emailMessage.getFromEmail());
			email.setSubject(emailMessage.getSubject());
			setBody(emailMessage.getBody());
		} catch (EmailException | AddressException | UnsupportedEncodingException e) {
			throw new EmailServiceException(e);
		}
	}

	protected abstract void setBody(String body) throws EmailException;

	protected abstract void attach(Collection<String> filesToAttach) throws EmailException;
	
    /**
     * Sends the email. Internally build a {@link MimeMessage}
     * which is afterwards sent to the SMTP server.
     *
     * @return the message id of the underlying MimeMessage
     * @throws EmailServiceException the sending failed
     */	
	public String send() throws EmailServiceException {
		try {
			return email.send();
		} catch (EmailException e) {
			throw new EmailServiceException(e);
		}
	}
	
	protected EmailAttachment createAttachment(String fileToAttach) {
		EmailAttachment attachment = new EmailAttachment();
		attachment.setPath(fileToAttach);
		attachment.setDisposition(EmailAttachment.ATTACHMENT);
		
		return attachment;
    }	
	
    private InternetAddress toInternetAddress(final String emailAddress) throws AddressException, UnsupportedEncodingException {
    	String result = WHITE_SPACES_PATTERN.matcher(emailAddress).replaceAll(EMPTY_STR);
    	
    	InternetAddress address = new InternetAddress(CONTROL_CHARACTERS_PATTERN.matcher(result).replaceAll(EMPTY_STR));
    	
    	address.validate();
        
        return address;
    }

}
