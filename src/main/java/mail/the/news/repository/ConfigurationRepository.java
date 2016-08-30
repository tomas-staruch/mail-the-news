package mail.the.news.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mail.the.news.domain.EmailServiceConfiguration;

public interface ConfigurationRepository extends JpaRepository<EmailServiceConfiguration, Long>, BaseRepository<EmailServiceConfiguration> {
	
    @Query("SELECT c FROM User u INNER JOIN u.configurations c WHERE u.email = :email")
    public Set<EmailServiceConfiguration> findByEmail(@Param("email") String email);

    @Query("SELECT c FROM User u INNER JOIN u.configurations c WHERE u.email = :email AND c.id = :id")
	public EmailServiceConfiguration findById(@Param("email") String email, @Param("id") Long configurationId);
}
