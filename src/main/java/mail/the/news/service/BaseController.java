package mail.the.news.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import mail.the.news.exception.EmailServiceException;
import mail.the.news.repository.BaseRepository;
import mail.the.news.repository.UserRepository;

/**
 * Base class for controllers with common logic
 */
public abstract class BaseController<T> {

	// TODO
	// https://www.mkyong.com/spring-mvc/spring-abstract-controller-example/
	// https://gist.github.com/wvuong/5673644
			
	@Autowired
	private UserRepository userRepository;

	protected UserRepository getUserRepository() {
		return userRepository;
	}
	
	protected abstract BaseRepository<T> getCustomRepository();
	
    /**
     * Get all resources of type T identified by user's email
     */
    @RequestMapping(method=RequestMethod.GET)
    public Set<T> read(@AuthenticationPrincipal final UserDetails authUser) throws EmailServiceException {
        return getCustomRepository().findByEmail(authUser.getUsername());
    }
    
	/**
     * Get a particular users's resource defined by id and user's email
     */
    @RequestMapping(path="/{id}",method=RequestMethod.GET)
    public T read(@PathVariable Long id, @AuthenticationPrincipal final UserDetails authUser) throws EmailServiceException {  	
    	// use user email to prevent asking of configuration which doesn't belongs to user
    	return getCustomRepository().findById(authUser.getUsername(), id);
    }

    @RequestMapping(method=RequestMethod.PUT)
    public T update(@Validated @RequestBody T configuration) throws EmailServiceException {
    	throw new UnsupportedOperationException("Not implemented yet");
    }
    
    @RequestMapping(method=RequestMethod.DELETE)
    public T delete(@Validated @RequestBody T configuration) throws EmailServiceException {
    	throw new UnsupportedOperationException("Not implemented yet");
    }
    
    protected HttpHeaders createHeaderWithLocation(Long id) {
		HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri());
		
        return headers;
	}
}