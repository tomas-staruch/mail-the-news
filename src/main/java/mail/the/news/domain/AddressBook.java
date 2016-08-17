package mail.the.news.domain;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="address_books")
public class AddressBook extends PersistentEntity implements Serializable {

	private static final long serialVersionUID = -1553834682239934105L;
	
	@Column(nullable=false)
	private String name; // TODO unique constraint name + user_id ?

	@ManyToOne(optional=false)
	@JoinColumn(name="user_id")
	@JsonBackReference
	private User user;
	
    @ManyToMany(fetch=FetchType.LAZY, cascade={CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name="addresses", joinColumns={@JoinColumn(name="address_book_id")}, inverseJoinColumns={@JoinColumn(name="email_address_id")})
	private Set<EmailAddress> addresses = new LinkedHashSet<>(0);
	
	AddressBook() { }

	public AddressBook(String name) {
		this.name = name;
	}
	
	public AddressBook(String name, Set<EmailAddress> addresses) {
		this.name = name;
		this.addresses = addresses;
	}

	public String getName() {
		return name;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public Set<EmailAddress> getAddresses() {
		return addresses;
	}

	public void addEmailAddress(EmailAddress emailAddress) {
		this.addresses.add(emailAddress);
	}

	@Override
	public int hashCode() {
		return 31 + getName().hashCode() * getUser().getEmail().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		
		AddressBook other = (AddressBook) obj;
		
		return getName().equals(other.getName()) && getUser().getEmail().equals(other.getUser().getEmail());
	}
	
	@Override
	public String toString() {
		return String.format("[name:%s, addresses:%d]", name, addresses.size());
	}
}
