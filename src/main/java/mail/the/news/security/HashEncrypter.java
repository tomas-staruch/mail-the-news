package mail.the.news.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import mail.the.news.exception.EmailSecurityException;

public class HashEncrypter implements Encrypter {
	private final PasswordEncoder encoder;
	
	@Autowired
	public HashEncrypter(PasswordEncoder encoder) {
		this.encoder = encoder;
	}

	@Override
	public String encrypt(String data) throws EmailSecurityException {
		return encoder.encode(data);
	}

	@Override
	public String decrypt(String data) throws EmailSecurityException {
		// the hashed data cannot be decrypted by PasswordEncoder
		return data;
	}
	
}
