package mail.the.news.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mail.the.news.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

	List<User> findByEmail(String email);
}
