package mail.the.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mail.the.news.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);
	
    @Query("SELECT CASE WHEN COUNT(1) > 0 THEN true ELSE false END FROM User u WHERE LOWER(u.email) = LOWER(:email)")
    public boolean existsByEmail(@Param("email") String email);
}
