package mail.the.news.security;

import mail.the.news.exception.EmailSecurityException;

/**
 * Interface for safe encryption and decryption of sensitive data
 */
public interface Encrypter { 
	
	public String encrypt(String data) throws EmailSecurityException;
	
	public String decrypt(String data) throws EmailSecurityException;
}
