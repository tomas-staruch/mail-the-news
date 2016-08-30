package mail.the.news.security;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Base64;

import org.junit.Test;

import mail.the.news.security.AesSymmetricKeyEncrypter.Seed;

public class PasswordEncrypterTest {
	
	@Test
	public void shouldEncryptAndDecryptWithRandomSeed() throws Exception {
		// given
		String dataToEncrypt = "sensitive string which has to be encrypted";
		AesSymmetricKeyEncrypter service = new AesSymmetricKeyEncrypter("mAsTerKey", new Seed());
		
		// when   	
    	String encrypted = service.encrypt(dataToEncrypt);
    	
		// then
		assertThat(service.decrypt(encrypted), is(dataToEncrypt));	
	}
	
	@Test
	public void shouldEncryptAndDecryptWithGivenSeed() throws Exception {
		// given
		String dataToEncrypt = "another sensitive string to encrypt";
		AesSymmetricKeyEncrypter service = new AesSymmetricKeyEncrypter("mAsTerKey", new Seed(Base64.getDecoder().decode("JUhHcgxfu0uUGEbrZe8xYWi/+DQz+Ar6Aqwb0J2s/8k=")));
		
		// when
    	String encrypted = service.encrypt(dataToEncrypt);
    	
		// then
		assertThat(service.decrypt(encrypted), is(dataToEncrypt));	
	}	
}
