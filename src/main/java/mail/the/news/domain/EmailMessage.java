package mail.the.news.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="email_messages")
public class EmailMessage {
	public enum ContentType {
		HTML, PLAIN_TEXT, MULTI_PART;
	}
	
	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne(optional = false, cascade={CascadeType.MERGE, CascadeType.REFRESH})
	private EmailAddress from;
	
	@ManyToOne(optional = false, cascade={CascadeType.MERGE, CascadeType.REFRESH})
	private EmailAddress to;
	
	private String subject, body;	
	
	@ElementCollection(fetch=FetchType.EAGER)
	private Set<String> filesToAttach; 
	
	@Column(nullable = false)
	private String encoding;
	
	@Enumerated(EnumType.STRING)
	private ContentType contentType;
	
	EmailMessage() { }
	
	public EmailMessage(EmailAddress from, EmailAddress to, String subject, String body, String encoding, ContentType contentType) {
		this.from = from;
		this.to = to;
		this.subject = subject;	
		this.body = body;	 
		this.encoding = encoding;
		this.contentType = contentType;
	}

	public EmailAddress getFrom() {
		return from;
	}

	public EmailAddress getTo() {
		return to;
	}

	public String getSubject() {
		return subject;
	}

	public String getBody() {
		return body;
	}
	
	public void setFilesToAttach(Set<String> filesToAttach) {
		this.filesToAttach = filesToAttach;
	}

	public Set<String> getFilesToAttach() {
		return filesToAttach;
	}

	public String getEncoding() {
		return encoding;
	}

	public ContentType getContentType() {
		return contentType;
	}

	@Override
	public int hashCode() {
		return 31 + from.hashCode() + to.hashCode() + subject.hashCode() + body.hashCode() + encoding.hashCode() + contentType.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		EmailMessage other = (EmailMessage) obj;
		
		return from.equals(other.getFrom()) && to.equals(other.getTo()) && subject.equals(other.getSubject()) && body.equals(other.getBody()) && encoding.equals(other.getEncoding()) && contentType.equals(other.getContentType());
	}

	@Override
	public String toString() {
		return String.format("[to:%s, from:%s, subject:%s, body:%s, attachment:%s, encoding:%s, type:%s]", to, from, subject, body, String.join(",", filesToAttach), encoding, contentType);
	}
}
