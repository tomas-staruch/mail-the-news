package mail.the.news.service;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import mail.the.news.domain.User;
import mail.the.news.service.exception.EmailServiceException;

@RestController
public class UserController {
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_!#$%&*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
	
	// see 
	// https://spring.io/blog/2014/12/02/latest-jackson-integration-improvements-in-spring
	// http://stackoverflow.com/questions/3325387/infinite-recursion-with-jackson-json-and-hibernate-jpa-issue
   
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
    @RequestMapping(path="/")
    public String home() {
        return "Mail the news REST API";
    }

    /**
     * Get details about user, configurations, emails, templates, etc.
     */
    @RequestMapping(path="/user", method=RequestMethod.GET)
    public User getUserSummary(@AuthenticationPrincipal final UserDetails authUser) {
        return userRepository.findByEmail(authUser.getUsername());
    }
    
    @RequestMapping(path="/user", method=RequestMethod.POST)
    public User registerUser(@RequestBody User user) throws EmailServiceException {
    	if(!isValid(user.getEmail())) {
    		throw new EmailServiceException("Given email address is not valid");
    	}
    	
    	if(userRepository.existsByEmail(user.getEmail())) {
    		throw new EmailServiceException("User with given email address is already registered");
    	}
    	
    	// encode password before it is saved into DB
    	user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.saveAndFlush(user);
    }

	private boolean isValid(String email) {
		return email != null && !email.isEmpty() && EMAIL_PATTERN.matcher(email).matches();
	}
}
