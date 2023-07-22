package mail.the.news.domain;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

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
	
	public String getUserName() {
		return getUser() != null ? getUser().getEmail() : null;
	}

	public Boolean isSslEnabled() {
		return sslEnabled;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getUrl(), getUserName(), getPort(), getPassword(), isSslEnabled());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		
		SmtpServiceConfiguration other = (SmtpServiceConfiguration) obj;
		
		return Objects.equals(getUrl(), other.getUrl()) &&
			   Objects.equals(getUserName(), other.getUserName()) && 
			   Objects.equals(getPort(), other.getPort()) && 
			   Objects.equals(getPassword(), other.getPassword()) && 				
			   Objects.equals(isSslEnabled(), other.isSslEnabled());
	}
	
	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).build();
	}
}
