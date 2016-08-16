package mail.the.news.service.provider;

import mail.the.news.domain.EmailServiceConfiguration;
import mail.the.news.domain.MailGunServiceConfiguration;
import mail.the.news.domain.SmtpServiceConfiguration;

import mail.the.news.service.provider.smtp.SmtpService;
import mail.the.news.service.provider.smtp.SmtpSessionBuilder;

public class EmailServiceFactory {

	public static EmailService buildService(EmailServiceConfiguration serviceConfig) {
		
		if(serviceConfig instanceof MailGunServiceConfiguration) {
			return new MailGunService((MailGunServiceConfiguration) serviceConfig);
		}
		else if(serviceConfig instanceof SmtpServiceConfiguration) {
			return new SmtpService(new SmtpSessionBuilder((SmtpServiceConfiguration) serviceConfig));
		}
		
		throw new UnsupportedOperationException("Missing service for given type of configuration");
	}
}
