package mail.the.news.security;

/**
 * Encryptor do nothing
 */
public class NullEncryptor implements Encrypter {

	@Override
	public String encrypt(String data) {
		return data;
	}

	@Override
	public String decrypt(String data) {
		return data;
	}
}
