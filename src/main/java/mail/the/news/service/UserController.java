package mail.the.news.service;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
	
    @RequestMapping(path="/")
    public String home() {
        return "Mail the news REST API";
    }

    /**
     * Get details about user, configurations, emails, templates, etc.
     */
    @RequestMapping(path="/user", method=RequestMethod.GET)
    public User read(@AuthenticationPrincipal final UserDetails authUser) {
    	User user = userRepository.findByEmail(authUser.getUsername());
    	// user password is required as encryption/decryption key
    	user.setPassword((String) SecurityContextHolder.getContext().getAuthentication().getCredentials()); 
    	
        return user;
    }
    
    /**
     * Create a new user
     */
    @RequestMapping(path="/user", method=RequestMethod.POST)
    public User create(@RequestBody User user) throws EmailServiceException {
    	if(!isValid(user.getEmail())) {
    		throw new EmailServiceException("Given email address is not valid");
    	}
    	
    	if(userRepository.existsByEmail(user.getEmail())) {
    		throw new EmailServiceException("User with given email address is already registered");
    	}

        return userRepository.saveAndFlush(user);
    }
    
    @RequestMapping(path="/user", method=RequestMethod.PUT)
    public User update(@RequestBody User user) throws EmailServiceException {
    	throw new UnsupportedOperationException("Not implemented yet");
    }
    
    @RequestMapping(path="/user", method=RequestMethod.DELETE)
    public User delete(@RequestBody User user) throws EmailServiceException {
    	throw new UnsupportedOperationException("Not implemented yet");
    }

	private boolean isValid(String email) {
		return email != null && !email.isEmpty() && EMAIL_PATTERN.matcher(email).matches();
	}
}
