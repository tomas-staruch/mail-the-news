package mail.the.news.domain;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import mail.the.news.service.provider.EmailService;

/**
 * The class represents the batch of emails which should be sent.
 * The emails are then logically grouped, so the history can be preserved.
 */
@Entity
@Table(name="email_batches")
public class EmailMessagesBatch extends PersistentEntity implements Serializable {

	private static final long serialVersionUID = -5643708356694065132L;
	
	public enum Status {
		CREATED, IN_PROGRESS, PROCESSED, INTERRUPTED;
	}

	@Enumerated(EnumType.STRING)
	private Status status = Status.CREATED;
	
	private String name;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="template_id", nullable=false)
	@JsonBackReference
	private EmailTemplate emailTemplate;
	
	@OneToMany(mappedBy="batch", cascade={CascadeType.ALL})
	@JsonManagedReference
	private Set<EmailMessage> emails = new LinkedHashSet<EmailMessage>(0);
	
	EmailMessagesBatch() { }

	public EmailMessagesBatch(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * TODO how to define to send e.g. first 100 only or re-send failed messages
	 */
	public Set<EmailMessage> sendAll(EmailService service) {
		emails.forEach(message -> message.send(service));

		return emails;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}
	
	public Set<EmailMessage> getEmails() {
		return this.emails;
	}
	
	protected void setEmails(Set<EmailMessage> emails) {
		this.emails = emails;
	}
	
	protected void addEmail(EmailMessage email) {
		if(email.getEmailMessagesBatch() != this)
			email.setEmailMessagesBatch(this);
		
		this.emails.add(email);
	}

	public EmailTemplate getEmailTemplate() {
		return emailTemplate;
	}

	protected void setEmailTemplate(EmailTemplate emailTemplate) {
		this.emailTemplate = emailTemplate;
	}
}
