package mail.the.news.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import mail.the.news.domain.User;
import mail.the.news.exception.EmailSecurityException;
import mail.the.news.repository.UserRepository;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
class WebSecurityConfig {

    @Autowired
    UserRepository userRepository;
    
	@Autowired
	PasswordEncoder passwordEncoder;

    /**
     * To allow a user to register himself, do not request authentication for POST method on "user" path
     */
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers(HttpMethod.POST, "/user");
    }

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// TODO HTTP Basic authentication is completely insecure unless the exchange was over a secure connection (HTTPS/TLS)
		http.authorizeRequests()
			.anyRequest()
			.authenticated().and()
			.httpBasic().and()
			.csrf().disable();

		return http.build();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	auth.eraseCredentials(false)
    		.userDetailsService(userDetailsService())
    		.passwordEncoder(passwordEncoder);
    }

	private UserDetailsService userDetailsService() {

	    return new UserDetailsService() {

			@Override
			public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
				User user = userRepository.findByEmail(email);

				if(user != null) {
					try {
						// to encrypt the password and compare the hashes is used autowired {@link PasswordEncoder}
						return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true, AuthorityUtils.createAuthorityList("USER"));
					} catch(EmailSecurityException e) {
						throw new UsernameNotFoundException("Could not find the user '" + email + "' because of an excaption as occured", e);
					}
				} else {
					throw new UsernameNotFoundException("Could not find the user '" + email + "'");
				}
			}
	    };
	}
}
