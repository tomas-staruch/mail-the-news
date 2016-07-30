package mail.the.news.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;


@Entity
@Table(name="email_addresses", indexes = {@Index(name = "index_email_addresses_email",  columnList="email", unique = true)})
public class EmailAddress {
	
	@Id
	@Column(unique = true, nullable = false)
	private String email;
	
	private String name;
	
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

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		// the name doesn't contribute to hashCode
		return email.hashCode();
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
		return String.format("[email:%s, name:%s]", email, name);
	}
}
