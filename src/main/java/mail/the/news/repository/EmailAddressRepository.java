package mail.the.news.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mail.the.news.domain.EmailAddress;

public interface EmailAddressRepository extends JpaRepository<EmailAddress, Long> {

	List<EmailAddress> findByEmail(String email); 
	
    @Query("SELECT CASE WHEN COUNT(1) > 0 THEN true ELSE false END FROM EmailAddress ea WHERE LOWER(ea.email) = LOWER(:email)")
    public boolean existsByEmail(@Param("email") String email);

}
