package mail.the.news.domain;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import mail.the.news.annotation.Encrypted;
import mail.the.news.security.listener.BCryptEncryptionListener;

@Entity
@EntityListeners(BCryptEncryptionListener.class)
@Table(name="users", indexes = {@Index(name = "index_on_user_email",  columnList="email", unique = true)})
public class User extends PersistentEntity implements Serializable {

	private static final long serialVersionUID = -4553824682239034107L;
	
	// email address uniquely identify the user and is used to set "from" of every email message
	@Column(unique=true, nullable=false)
	private String email;

	private String name;
	
	@Transient
	private String password; // user password is used as key for encryption of other passwords
	
	@Column(nullable=false)
	@Encrypted
	private String hashedPassword;
	
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
		this.email = email;
		this.name = name;
		this.password = password;
		this.hashedPassword = password;
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
	
	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	@JsonIgnore
	public String getPassword() {
		return this.password;
	}
	
	@JsonIgnore
	public String getHashedPassword() {
		return hashedPassword;
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
		return 31 + getEmail().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		
		User other = (User) obj;
		
		return getEmail().equals(other.getEmail());
	}
}
