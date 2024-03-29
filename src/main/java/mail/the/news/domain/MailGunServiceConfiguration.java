package mail.the.news.domain;

import java.io.Serializable;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("mail_gun")
public class MailGunServiceConfiguration extends EmailServiceConfiguration implements Serializable {

	private static final long serialVersionUID = 4883091064714965124L;
	
	public MailGunServiceConfiguration() { }
	
	public MailGunServiceConfiguration(String url, String apiKey) { 
		super(url, apiKey);
	}
}
