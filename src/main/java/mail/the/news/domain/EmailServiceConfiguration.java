package mail.the.news.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @Type(value = MailGunServiceConfiguration.class, name = "mail_gun"),
    @Type(value = SmtpServiceConfiguration.class, name = "smtp") 
})
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="config_type")
@Table(name="email_service_configurations")
public abstract class EmailServiceConfiguration extends SecurePersistentEntity {
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	@JsonBackReference
	protected User user;

	@NotNull(message="error.url.notnull")
	@Size(min=4,max=255,message="error.url.size")
	@Column(nullable=false)
	protected String url;
	
	EmailServiceConfiguration() {}
	
	protected EmailServiceConfiguration(String url, String password) {
		super(password);
		this.url = url;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String getUrl() {
		return url;
	}
}
