package mail.the.news.domain;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

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
	
	private String hostName;
	
	private Integer port;
	
	private String pwd;
	
	private Boolean sslEnabled;
	
	SmtpServiceConfiguration() { }

	public SmtpServiceConfiguration(String hostName, String pwd) {
		this(hostName, DefaultPorts.NON_ENCRYPTED.getPort(), pwd, Boolean.FALSE);
	}
	
	public SmtpServiceConfiguration(String hostName, Integer port, String pwd) {
		this(hostName, port, pwd, Boolean.FALSE);
	}
	
	public SmtpServiceConfiguration(String hostName, Integer port, String pwd, Boolean sslEnabled) {
		this.hostName = hostName;
		this.port = port;
		this.pwd = pwd;
		this.sslEnabled = sslEnabled;
	}

	public String getHostName() {
		return hostName;
	}

	public Integer getPort() {
		return port;
	}
	
	public String getUserName() {
		return getUser().getEmail();
	}

	public String getPwd() {
		return pwd;
	}

	public Boolean isSslEnabled() {
		return sslEnabled;
	}
	
	@Override
	public int hashCode() {
		return 31 + getHostName().hashCode() * getUserName().hashCode() * getPort().hashCode() * getPwd().hashCode() * isSslEnabled().hashCode();
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
			   getPort().equals(other.getPort()) && 				
			   isSslEnabled().equals(other.isSslEnabled());
	}
}
