package mail.the.news.service;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import mail.the.news.domain.User;
import mail.the.news.repository.UserRepository;
import mail.the.news.security.HashEncrypter;

@RestController
@RequestMapping(path="/user")
public class UserController {
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_!#$%&*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

	private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Get details about authenticated user, configurations, emails, templates, etc.
     */
    @RequestMapping(method=RequestMethod.GET)
    public User read(@AuthenticationPrincipal final UserDetails authUser) {
        return userRepository.findByEmail(authUser.getUsername());
    }

    /**
     * Get details about authenticated user in the same way as {@link #read(UserDetails)}.
     * Also perform additional check of user id to prevent
     */
    @RequestMapping(path="/{id}", method=RequestMethod.GET)
    public User read(@PathVariable Long id, @AuthenticationPrincipal final UserDetails authUser) {
        User user = userRepository.findByEmail(authUser.getUsername());

        if(!user.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Requested user id doesn't belong to authenticated user");
        }
        return user;
    }
    
    /**
     * Create a new user
     * 
     * Note: creation of user is excluded from authentication process
     */
    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity<User> create(@Validated @RequestBody User user) {
        if(StringUtils.isBlank(user.getEmail()) || !EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Given email address is not valid");
        }
    	
    	if(userRepository.existsByEmail(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with given email exists");
    	}

        User result = userRepository.saveAndFlush(new User(user.getEmail(), user.getName(), new HashEncrypter(passwordEncoder).encrypt(user.getPassword())));
        
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId()).toUri());

        return new ResponseEntity<>(result, headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(method=RequestMethod.PUT)
    public User update(@Validated @RequestBody User user) {
        throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Not implemented yet");
    }
    
    @RequestMapping(method=RequestMethod.DELETE)
    public User delete(@Validated @RequestBody User user) {
    	throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Not implemented yet");
    }
}