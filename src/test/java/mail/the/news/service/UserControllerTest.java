package mail.the.news.service;

import static org.junit.Assert.assertEquals;

import java.util.Base64;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
	PasswordEncoder passwordEncoder;
    
    @Test
    public void shouldFindUser() {
    	// given credentials of existing user (see TestingUserLoader)
    	String plainCreds = "dummy_user@not.existing.domain.com:RaNdOmPwD";

        // when
        ResponseEntity<String> response = restTemplate.exchange("/user", HttpMethod.GET, createUserAuthRequest(plainCreds), String.class);
        
        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void shouldCreateNewUser() {
    	// given a user in JSON format (because password wouldn't be parsed into User object)
    	HttpEntity<String> entity = createUserRequest("joe.black@his.domain.com", "Joe Black", "eNcRyPtEdPwD");

        // when
		ResponseEntity<String> response = restTemplate.exchange("/user", HttpMethod.POST, entity, String.class);
        
        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
    
    private HttpEntity<String> createUserAuthRequest(String plainCreds) {
    	HttpHeaders headers = new HttpHeaders();
    	headers.add("Authorization", "Basic " + new String(Base64.getEncoder().encode(plainCreds.getBytes())));
		
		return new HttpEntity<String>(headers);
    }
    
    private HttpEntity<String> createUserRequest(String email, String name, String pwd) {
    	JSONObject request = new JSONObject();
    	request.put("email", email);
    	request.put("name", name);
    	request.put("password", pwd);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		return new HttpEntity<String>(request.toString(), headers);
    }
}
