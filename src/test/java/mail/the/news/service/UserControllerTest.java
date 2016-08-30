package mail.the.news.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import mail.the.news.domain.User;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldCreateNewUser() {
    	// given
        User user = new User("user@domain.com", "eNcRyPtEdPaSsWoRd");
        
        // when
        ResponseEntity<Void> response = restTemplate.postForEntity("/user", user, Void.class);
        
        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}
