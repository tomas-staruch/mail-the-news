package mail.the.news.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import mail.the.news.domain.EmailTemplate;
import mail.the.news.repository.TemplateRepository;

@RestController
@RequestMapping(path="/user/templates")
public class TemplateController extends BaseController<EmailTemplate> {
	
	@Autowired
	protected TemplateRepository templateRepository;

    /**
     * Create a new configuration and add it to the list of user's configurations 
     */
    @RequestMapping(method=RequestMethod.POST)
    public EmailTemplate create(@AuthenticationPrincipal final UserDetails authUser, @Validated @RequestBody EmailTemplate template) throws Exception {
    	if(template.getUser() == null) {
    		template.setUser(getUserRepository().findByEmail(authUser.getUsername()));
    	}
	
        return templateRepository.saveAndFlush(template);
    }

	@Override
	protected TemplateRepository getCustomRepository() {
		return templateRepository;
	}
}
