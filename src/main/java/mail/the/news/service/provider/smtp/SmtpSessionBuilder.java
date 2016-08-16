package mail.the.news.service.provider.smtp;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import mail.the.news.domain.SmtpServiceConfiguration;
import mail.the.news.service.provider.SessionBuilder;

/**
 * Build from a given configuration {@link Session} object and store it in a map for further usage
 */
public class SmtpSessionBuilder implements SessionBuilder<Session> {
	private static final Map<SmtpServiceConfiguration, Session> sessions = new HashMap<>();
	
	private final SmtpServiceConfiguration config;
	
	public SmtpSessionBuilder(SmtpServiceConfiguration config) {
		this.config = config;
	}
	
	public Session buildSession() {
		if(!sessions.containsKey(this.config)) {
			sessions.put(this.config, buildSession(this.config));
		}
		
		return sessions.get(this.config);
	}
    
	private Session buildSession(SmtpServiceConfiguration config) {
		Properties properties = buildProperities(config.isSslEnabled(), true, config.getHostName(), config.getPort());
		Authenticator authenticator = buildAuthenticator(config.getUserName(), config.getPwd());
		
		Session session = Session.getInstance(properties, authenticator);
		session.setDebug(true);

		return session;
	}
	
	private Authenticator buildAuthenticator(String userName, String password) {
		return new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password);
			}
		};
	}

	private Properties buildProperities(boolean enableSsl, boolean enableAuthentication, String host, Integer port) {
		Properties properties = System.getProperties();
		properties.put("mail.smtp.auth", enableAuthentication ? Boolean.TRUE.toString() : Boolean.FALSE.toString());
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);		
		properties.put(enableSsl ? "mail.smtp.ssl.enable" : "mail.smtp.starttls.enable", Boolean.TRUE.toString());
		
		return properties;
	}
}
