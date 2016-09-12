package mail.the.news.service;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import mail.the.news.domain.User;
import mail.the.news.exception.EmailServiceException;
import mail.the.news.repository.UserRepository;
import mail.the.news.security.HashEncrypter;

@RestController
@RequestMapping(path="/user")
public class UserController {
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_!#$%&*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

	@Autowired
	protected UserRepository userRepository;

	@Autowired
	protected PasswordEncoder passwordEncoder;

    /**
     * Get details about user, configurations, emails, templates, etc.
     * @throws EmailServiceException 
     */
    @RequestMapping(method=RequestMethod.GET)
    public User read(@AuthenticationPrincipal final UserDetails authUser) throws EmailServiceException {
        return userRepository.findByEmail(authUser.getUsername());
    }
    
    /**
     * Create a new user
     * 
     * Note: creation of user is excluded from authentication process
     */
    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity<User> create(@Validated @RequestBody User user) throws EmailServiceException {
    	validate(user.getEmail());
    	
    	if(userRepository.existsByEmail(user.getEmail())) {
    		throw new EmailServiceException("User with given email address is already registered");
    	}
    	
    	user.setPassword(new HashEncrypter(passwordEncoder).encrypt(user.getPassword()));
	
        User result = userRepository.saveAndFlush(user);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId()).toUri());

        return new ResponseEntity<>(result, headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(method=RequestMethod.PUT)
    public User update(@Validated @RequestBody User user) throws EmailServiceException {
    	throw new UnsupportedOperationException("Not implemented yet");
    }
    
    @RequestMapping(method=RequestMethod.DELETE)
    public User delete(@Validated @RequestBody User user) throws EmailServiceException {
    	throw new UnsupportedOperationException("Not implemented yet");
    }
    
	private void validate(String email) throws EmailServiceException {
		if(email == null || email.isEmpty() || !EMAIL_PATTERN.matcher(email).matches()) {
			throw new EmailServiceException("Given email address is not valid");
		}
	}
}
