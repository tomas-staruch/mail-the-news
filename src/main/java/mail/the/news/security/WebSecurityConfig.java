package mail.the.news.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import mail.the.news.domain.User;
import mail.the.news.service.UserRepository;

@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserRepository userRepository;
    
	@Autowired
	PasswordEncoder passwordEncoder;

    /**
     * To allow a user to register himself, do not request authentication for POST method on "user" path
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.POST, "/user");
    }
 
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.anyRequest()
			.authenticated().and()
			.httpBasic().and()
			.csrf().disable();
	}
	
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	auth.eraseCredentials(false)
    		.userDetailsService(userDetailsService())
    		.passwordEncoder(passwordEncoder);
    }

	
	@Bean
	protected UserDetailsService userDetailsService() {

	    return new UserDetailsService() {

			@Override
			public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
				User user = userRepository.findByEmail(email);
				
				if(user != null) {
					// the password hashes are compared at DaoAuthenticationProvider
			        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getHashedPassword(), user.isEnabled(), true, true, true, AuthorityUtils.createAuthorityList("USER"));
				} else {
					throw new UsernameNotFoundException("Could not find the user '" + email + "'");
				}
			}
	    };
	}
}
