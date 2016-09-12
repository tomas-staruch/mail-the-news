package mail.the.news.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import mail.the.news.domain.EmailServiceConfiguration;
import mail.the.news.repository.ConfigurationRepository;
import mail.the.news.security.AesSymmetricKeyEncrypter;
import mail.the.news.security.Encrypter;

@RestController
@RequestMapping(path="/user/configurations")
public class ConfigurationController extends BaseController<EmailServiceConfiguration> {
	
	@Autowired
	protected ConfigurationRepository configurationRepository;
    
    /**
     * Create a new configuration and add it to the list of user's configurations 
     */
    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity<EmailServiceConfiguration> create(@AuthenticationPrincipal final UserDetails authUser, @Validated @RequestBody EmailServiceConfiguration configuration) throws Exception {
    	// user's password is required as key for symmetric encryption/decryption  	
    	String securityKey = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials(); 
    	Encrypter encrypter = new AesSymmetricKeyEncrypter(securityKey, new AesSymmetricKeyEncrypter.Seed());
    	configuration.setPassword(encrypter.encrypt(configuration.getPassword()));
    	
    	if(configuration.getUser() == null) {
    		configuration.setUser(getUserRepository().findByEmail(authUser.getUsername()));
    	}
    	
    	EmailServiceConfiguration result = configurationRepository.saveAndFlush(configuration);

        return new ResponseEntity<>(result, createHeaderWithLocation(result.getId()), HttpStatus.CREATED);
    }
    
    @Override
    protected ConfigurationRepository getCustomRepository() {
    	return configurationRepository;
    }
}
