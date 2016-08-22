package mail.the.news.domain;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import mail.the.news.annotation.Encrypted;
import mail.the.news.security.EncryptionKey;
import mail.the.news.security.listener.AesEncryptionListener;

@Entity
@EntityListeners(AesEncryptionListener.class)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="config_type")
@Table(name="email_service_configurations")
public abstract class EmailServiceConfiguration extends PersistentEntity implements EncryptionKey {
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	@JsonBackReference
	protected User user;
	
	protected String url;
	
	EmailServiceConfiguration() {}
	
	protected EmailServiceConfiguration(String url, String password) {
		this.url = url;
		this.password = password;
	}

	@Encrypted
	protected String password;

	public User getUser() {
		return this.user;
	}
	
	@JsonIgnore
	@Override
	public String getEncryptionKey() {
		return getUser().getPassword();
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	protected String getUrl() {
		return url;
	}

	@JsonIgnore
	protected String getPassword() {
		return password;
	}
}
