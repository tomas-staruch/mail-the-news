package mail.the.news.service;

import mail.the.news.domain.EmailAddress;

public interface EmailAddressRepositoryCustom {
	public EmailAddress findOrCreate(EmailAddress e);
}
