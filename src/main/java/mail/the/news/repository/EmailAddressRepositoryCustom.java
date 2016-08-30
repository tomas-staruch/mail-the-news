package mail.the.news.repository;

import mail.the.news.domain.EmailAddress;

public interface EmailAddressRepositoryCustom {
	public EmailAddress findOrCreate(EmailAddress e);
}
