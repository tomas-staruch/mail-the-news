package mail.the.news.domain;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import mail.the.news.domain.EmailTemplate.ContentType;
import mail.the.news.service.provider.EmailService;

/*
 * Class represents a single email message with one recipient.
 */

@Entity
@Table(name="email_messages")
public class EmailMessage extends PersistentEntity implements Serializable {

	private static final long serialVersionUID = 3257255561482843237L;
	
	public enum Status {
		CREATED, SENT, FAILED;
	}
	
	private String from_email, to_email;
	private String subject, body;	
	
	@Enumerated(EnumType.STRING)
	private Status status = Status.CREATED;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="batch_id", nullable=false)
	private EmailMessagesBatch batch;
	
	EmailMessage() { }
	
	public EmailMessage(String from, String to, String subject, String body) {
		this.from_email = from;
		this.to_email = to;
		this.subject = subject;	
		this.body = body;	 
	}
	

	public EmailMessage send(EmailService service) {
		return service.send(this);
	}

	public String getFromEmail() {
		return from_email;
	}
	
	protected void setFromEmail(String from) {
		this.from_email = from;
	}

	public String getToEmail() {
		return to_email;
	}
	
	protected void setToEmail(String to) {
		this.to_email = to;
	}

	public String getSubject() {
		return subject;
	}
	
	protected void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}
	
	protected void setBody(String body) {
		this.body = body;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public EmailMessagesBatch getEmailMessagesBatch() {
		return this.batch;
	}

	public void setEmailMessagesBatch(EmailMessagesBatch batch) {
		this.batch = batch;
	}

	@Override
	public int hashCode() {
		return 31 + from_email.hashCode() * to_email.hashCode() * subject.hashCode() * body.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		EmailMessage other = (EmailMessage) obj;
		
		return from_email.equals(other.getFromEmail()) && to_email.equals(other.getToEmail()) && subject.equals(other.getSubject()) && body.equals(other.getBody());
	}

	@Override
	public String toString() {
		return String.format("[to:%s, from:%s, subject:%s, body:%s, attachment:%s]", to_email, from_email, subject, body);
	}

	public ContentType getContentType() {
		return batch.getEmailTemplate().getContentType();
	}

	public String getEncoding() {
		return batch.getEmailTemplate().getEncoding();
	}

	public Collection<String> getFilesToAttach() {
		return batch.getEmailTemplate().getFilesToAttach();
	}
}
