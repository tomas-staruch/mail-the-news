package mail.the.news;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
		// GET json or xml
		// curl -k --header "Accept: application/json" --user info@originalnapredaj.sk:random_password https://localhost:9000/user
		// curl -k --header "Accept: application/json" --user tomas.staruch@gmail.com:random_password https://localhost:9000/user
		//
		// POST json
		// curl -k -H "Content-Type: application/json" --data "{\"email\":\"tomas.staruch@gmail.com\",\"password\":\"random_password\",\"name\":\"Tomas Staruch\"}" https://localhost:9000/user
		//
		
		// see Product Loader
		// https://springframework.guru/spring-boot-web-application-part-3-spring-data-jpa/
}
