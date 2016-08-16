package mail.the.news.domain;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("mail_gun")
public class MailGunServiceConfiguration extends EmailServiceConfiguration implements Serializable {

	private static final long serialVersionUID = 4883091064714965124L;
	
	private String serviceUrl;
	
	private Integer apiKey;
	
	public MailGunServiceConfiguration() { }

	public MailGunServiceConfiguration(String serviceUrl, Integer apiKey) {
		this.serviceUrl = serviceUrl;
		this.apiKey = apiKey;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public Integer getApiKey() {
		return apiKey;
	}
}
