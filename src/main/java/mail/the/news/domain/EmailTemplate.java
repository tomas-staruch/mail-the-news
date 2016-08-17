package mail.the.news.domain;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
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

/**
 * The template for email messages
 */
@Entity
@Table(name="email_templates")
public class EmailTemplate extends PersistentEntity implements Serializable {

	private static final long serialVersionUID = 1657534653607504816L;
	
	private static String DEFAULT_ENCODING = "UTF-8";
	
	public enum ContentType {
		HTML, PLAIN_TEXT, MULTI_PART;
	}
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	@JsonBackReference
	private User user;

	@ManyToOne
	@JoinColumn(name="address_book_id")
	private AddressBook addressBook;
	
	private String subject, body;	
	
	@ElementCollection
	private Set<String> filesToAttach = new LinkedHashSet<>(0);
	
	@Column(nullable = false)
	private String encoding = DEFAULT_ENCODING;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ContentType contentType = ContentType.PLAIN_TEXT;
	
	@OneToMany(mappedBy="emailTemplate", cascade={CascadeType.ALL})
	@JsonManagedReference
	private Set<EmailMessagesBatch> batches = new LinkedHashSet<>(0);
	
	EmailTemplate() { }
	
	public EmailTemplate(String subject, String body) {
		this(subject, body, DEFAULT_ENCODING, ContentType.PLAIN_TEXT);
	}
	
	public EmailTemplate(String subject, String body, String encoding, ContentType contentType) {
		this.subject = subject;	
		this.body = body;	 
		this.encoding = encoding;
		this.contentType = contentType;
	}
	
	public User getUser() {
		return this.user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public void setAddressBook(AddressBook addressBook) {
		this.addressBook = addressBook;
	}
	
	public String getUserEmail() {
		return this.user.getEmail();
	}
	
	public Set<EmailAddress> getAddresses() {
		return this.addressBook.getAddresses();
	}

	public String getSubject() {
		return this.subject;
	}
	
	protected void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return this.body;
	}
	
	protected void setBody(String body) {
		this.body = body;
	}
	
	/**
	 * Create from the template a set of email messages and store them in batch
	 */
	public EmailMessagesBatch createBatch(String name) {
		EmailMessagesBatch batch = new EmailMessagesBatch(name);
		addBatch(batch);
		getAddresses().forEach(recipient -> batch.addEmail(new EmailMessage(getUserEmail(), recipient.getEmail(), this.getSubject(), this.getBody())));
		
		return batch;
	}
	
	public void addBatch(EmailMessagesBatch batch) {
		if(batch.getEmailTemplate() != this) {
			batch.setEmailTemplate(this);
		}
		this.batches.add(batch);
	}
	
	public Set<EmailMessagesBatch> getBatches() {
		return this.batches;
	}

	public Set<String> getFilesToAttach() {
		return this.filesToAttach;
	}
		
	public void setFilesToAttach(Set<String> filesToAttach) {
		this.filesToAttach = filesToAttach;
	}
	
	public void addFileToAttach(String fileToAttach) {
		this.filesToAttach.add(fileToAttach);
	}
	
	public String getEncoding() {
		return this.encoding;
	}	

	public ContentType getContentType() {
		return this.contentType;
	}

	@Override
	public int hashCode() {
		return 31 + getUserEmail().hashCode() * getAddresses().size() * subject.hashCode() * body.hashCode() * encoding.hashCode() * contentType.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		EmailTemplate other = (EmailTemplate) obj;
		
		return getUserEmail().equals(other.getUserEmail()) && getAddresses().containsAll(other.getAddresses()) && subject.equals(other.getSubject()) && body.equals(other.getBody()) && encoding.equals(other.getEncoding()) && contentType.equals(other.getContentType())&& filesToAttach.equals(other.getFilesToAttach());
	}

	@Override
	public String toString() {
		return String.format("EmailTemplate[to:%s, from:%s,\n subject:%s,\n body:%s,\n attachment:%s,\n encoding:%s, type:%s]", 
				getAddresses().stream().map(EmailAddress::getEmail).collect(Collectors.joining(", ")), 
				getUserEmail(), 
				getSubject(), 
				getBody(), 
				String.join(",", getFilesToAttach()), 
				getEncoding(), 
				getContentType());
	}
}
