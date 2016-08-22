package mail.the.news.security.listener;

import java.lang.reflect.Field;

import javax.persistence.PrePersist;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import mail.the.news.annotation.Encrypted;

/**
 * JPA listener which uses BCrypt hashing function to encrypt the value of annotated {link @Encrypted} field
 *
 * Note: it's not possible to inject spring managed beans into a JPA EntityListener class. 
 */
public class BCryptEncryptionListener {

	/**
	 * Encode a string filed annotated by {link @Encrypted}
	 */
    @PrePersist
	public <T> void encrypt(T entity) throws IllegalArgumentException, IllegalAccessException {
		for(Field field : entity.getClass().getDeclaredFields()) {
    		if (field.isAnnotationPresent(Encrypted.class)) {
    			field.setAccessible(true);
    			field.set(entity, new BCryptPasswordEncoder().encode((String)field.get(entity)));
    		}
		}
	}
}
