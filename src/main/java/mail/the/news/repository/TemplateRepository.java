package mail.the.news.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mail.the.news.domain.EmailServiceConfiguration;
import mail.the.news.domain.EmailTemplate;

public interface TemplateRepository extends JpaRepository<EmailTemplate, Long>, BaseRepository<EmailTemplate> {
	
    @Query("SELECT et FROM User u INNER JOIN u.emailTemplates et WHERE u.email = :email")
    public Set<EmailTemplate> findByEmail(@Param("email") String email);

    @Query("SELECT et FROM User u INNER JOIN u.emailTemplates et WHERE u.email = :email AND et.id = :id")
	public EmailTemplate findById(@Param("email") String email, @Param("id") Long templateId);

}
