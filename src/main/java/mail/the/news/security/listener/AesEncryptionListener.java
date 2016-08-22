package mail.the.news.security.listener;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.PostLoad;
import javax.persistence.PrePersist;

import mail.the.news.annotation.Encrypted;
import mail.the.news.security.AesEncrypter;
import mail.the.news.security.EncryptionKey;

/**
 * JPA listener which uses AES symmetric-key algorithm to encrypt and decrypt the value of annotated {link @Encrypted} field.
 * AES algorithm requires a key which is used to encrypt and decrypt, that's why entity has to extend {link @EncryptionKey}. 
 *
 * Note: it's not possible to inject spring managed beans into a JPA EntityListener class. 
 */
public class AesEncryptionListener {

	/**
	 * Encode a string filed annotated by {link @Encrypted}
	 */
    @PrePersist
	public <T extends EncryptionKey> void encrypt(T entity) throws Exception {
    	List<Field> filteredFields = getAllFields(new LinkedList<Field>(), entity.getClass(), Encrypted.class);
    	
		for(Field field : filteredFields) {
			field.setAccessible(true);
			String valueOfFiled = (String)field.get(entity);
			String newNlue = new AesEncrypter(entity.getEncryptionKey(), new AesEncrypter.Seed()).encrypt(valueOfFiled);

			field.set(entity, newNlue);
		}
	}
    
    private List<Field> getAllFields(final List<Field> fields, Class<?> type, Class<? extends Annotation> annotatiedBy) {
        Arrays.asList(type.getDeclaredFields()).forEach(field -> { 
        	if(field.isAnnotationPresent(annotatiedBy)) {
        		fields.add(field);
        	}
        });

        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass(), annotatiedBy);
        }

        return fields;
    }
    
	/**
	 * Encode a string filed annotated by {link @Encrypted}
	 */
    @PostLoad
	public <T extends EncryptionKey> void decrypt(T entity) throws Exception {
    	List<Field> filteredFields = getAllFields(new LinkedList<Field>(), entity.getClass(), Encrypted.class);
    	
		for(Field field : filteredFields) {
			field.setAccessible(true);
			byte[] valueOfFiled = Base64.getDecoder().decode((String)field.get(entity));
			byte[] seed = Arrays.copyOfRange(valueOfFiled, 0, AesEncrypter.Seed.SEED_LENGTH);
			byte[] data = Arrays.copyOfRange(valueOfFiled, AesEncrypter.Seed.SEED_LENGTH, valueOfFiled.length);

			field.set(entity, new AesEncrypter(entity.getEncryptionKey(), new AesEncrypter.Seed(seed)).decrypt(data));
		}
	}
}
