package mail.the.news.domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;


@Entity
@Table(name="email_addresses", indexes = {@Index(name = "index_on_email_address",  columnList="email", unique = true)})
public class EmailAddress extends PersistentEntity implements Serializable {

	private static final long serialVersionUID = -1738609278576442083L;
	
	public enum Status {
		ENABLED, DISABLED, REMOVED, INVALID;
	}

	@Column(unique = true, nullable = false)
	private String email;
	
	private String name;
	
	@Enumerated(EnumType.STRING)
	private Status status = Status.ENABLED;
	
	EmailAddress() { }
	
	public EmailAddress(String email) {
		this(email, null);
	}
	
	public EmailAddress(String email, String name) {
		this.email = email;
		this.name = name;
	}

	public String getEmail() {
		return email;
	}
	
	protected void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}
	
	protected void setName(String name) {
		this.name = name;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		// the name doesn't contribute to hashCode
		return 31 + email.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		EmailAddress other = (EmailAddress) obj;
		
		return email.equals(other.email); // the name is ignored
	}
	
	@Override
	public String toString() {
		return String.format("[email:%s, name:%s, status:%s]", email, name, status);
	}
}
