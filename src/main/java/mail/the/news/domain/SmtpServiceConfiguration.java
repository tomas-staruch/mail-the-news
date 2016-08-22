package mail.the.news.domain;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;

import mail.the.news.security.listener.AesEncryptionListener;

@Entity
@DiscriminatorValue("smtp")
public class SmtpServiceConfiguration extends EmailServiceConfiguration implements Serializable {

	private static final long serialVersionUID = -6335850073163236586L;
	
	private enum DefaultPorts {
		NON_ENCRYPTED(25), TLS(587), SSL(465);
		
		private final Integer port;
		
		private DefaultPorts(Integer port) {
			this.port = port;
		}
		
		public Integer getPort() {
			return port;
		}
	}
	
	private Integer port;

	private Boolean sslEnabled;
	
	SmtpServiceConfiguration() { }

	public SmtpServiceConfiguration(String hostName, String password) {
		this(hostName, DefaultPorts.NON_ENCRYPTED.getPort(), password, Boolean.FALSE);
	}
	
	public SmtpServiceConfiguration(String hostName, Integer port, String password) {
		this(hostName, port, password, Boolean.FALSE);
	}
	
	public SmtpServiceConfiguration(String hostName, Integer port, String password, Boolean sslEnabled) {
		super(hostName, password);
		this.port = port;
		this.sslEnabled = sslEnabled;
	}

	public Integer getPort() {
		return port;
	}
	
	public String getHostName() {
		return super.getUrl();
	}
	
	public String getUserName() {
		return getUser().getEmail();
	}

	@Override
	public String getPassword() {
		return password;
	}

	public Boolean isSslEnabled() {
		return sslEnabled;
	}
	
	@Override
	public int hashCode() {
		return 31 +getHostName().hashCode() * getUserName().hashCode() * getPort().hashCode() * getPassword().hashCode() * isSslEnabled().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		
		SmtpServiceConfiguration other = (SmtpServiceConfiguration) obj;
		
		return getHostName().equals(other.getHostName()) &&
			   getUserName().equals(other.getUserName()) && 
			   getPort().equals(other.getPort()) && 
			   getPassword().equals(other.getPassword()) && 				
			   isSslEnabled().equals(other.isSslEnabled());
	}
}
