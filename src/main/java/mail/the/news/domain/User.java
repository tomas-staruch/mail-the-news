package mail.the.news.domain;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="users", indexes = {@Index(name = "index_on_user_email",  columnList="email", unique = true)})
public class User extends SecurePersistentEntity implements Serializable {

	private static final long serialVersionUID = -4553824682239034107L;
	
	// email address uniquely identify the user and is used to set "from" of every email message
	@NotNull(message = "error.email.notnull")
	@Size(min = 3, max = 255, message = "error.email.size")
	@Column(unique=true, nullable=false)
	private String email;

	private String name;
	
	private Boolean enabled = Boolean.TRUE;
	
	@OneToMany(mappedBy="user", cascade=CascadeType.ALL)
	@JsonManagedReference
	private Set<EmailTemplate> emailTemplates = new LinkedHashSet<>(0);
	
	@OneToMany(mappedBy="user", cascade=CascadeType.ALL)
	@JsonManagedReference
	private Set<AddressBook> addressBooks = new LinkedHashSet<>(0);
	
	@OneToMany(mappedBy="user", cascade=CascadeType.ALL)
	@JsonManagedReference
	Set<EmailServiceConfiguration> configurations = new LinkedHashSet<>(0);
	
	User() { }
	
	public User(String email, String password) {
		this(email, null, password);
	}
	
	public User(String email, String name, String password) {
		super(password);
		this.email = email;
		this.name = name;
	}

	public EmailMessagesBatch composeMessages(EmailTemplate template, String name) {
		return template.createBatch(name);
	}
	
	public String getEmail() {
		return this.email;
	}

	public String getName() {
		return this.name;
	}
	
	public Boolean isEnabled() {
		return this.enabled;
	}
	
	public Set<EmailTemplate> getEmailTemplates() {
		return this.emailTemplates;
	}
	
	public void addEmailTemplate(EmailTemplate emailTemplate) {
		if(emailTemplate.getUser() != this) {
			emailTemplate.setUser(this);
		}
		
		this.emailTemplates.add(emailTemplate);
	}
	
	public Set<AddressBook> getAddressBooks() {
		return this.addressBooks;
	}
	
	public void addAddressBook(AddressBook addressBook) {
		if(addressBook.getUser() != this) {
			addressBook.setUser(this);
		}
		
		this.addressBooks.add(addressBook);
	}
	
	public Set<EmailServiceConfiguration> getConfigurations() {
		return this.configurations;
	}
	
	public void addConfiguration(EmailServiceConfiguration configuration) {
		if(configuration.getUser() != this) {
			configuration.setUser(this);
		}
		
		this.configurations.add(configuration);
	}	
	
	@Override
	public int hashCode() {
		return Objects.hash(getEmail());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		
		User other = (User) obj;
		
		return Objects.equals(getEmail(), other.getEmail());
	}
}
