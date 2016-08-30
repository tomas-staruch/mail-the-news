package mail.the.news.security;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import mail.the.news.exception.EmailSecurityException;

/**
 * Class provides methods for safe encryption and decryption of sensitive data by AES symmetric-key algorithm. 
 * The password is used for both encrypting and decrypting the data.
 * 
 * TODO see AesBytesEncryptor
 */
public class AesSymmetricKeyEncrypter implements Encrypter {
	
	/**
	 * Divide 32 byte seed to two equal parts. 
	 * Use the first as salt and the second as initialization vector
	 */
	public static final class Seed {
	    public static final int SEED_LENGTH = 32;
	    
		private final byte[] seed;
	    
	    /**
	     * Init by new random seed
	     */
	    public Seed() {
			this(new SecureRandom().generateSeed(SEED_LENGTH));
		}
	    
	    /**
	     * Init by array of bytes
	     */    
		public Seed(byte[] seed) {
			if(seed.length < SEED_LENGTH) {
				throw new IllegalArgumentException("The length of seed has to be greater than " + SEED_LENGTH);
			}
			this.seed = seed;
		}
		
		public byte[] getSalt() {
			return Arrays.copyOfRange(seed, 0, SEED_LENGTH/2);
		}
		
		public byte[] getInitializationVector() {
			return Arrays.copyOfRange(seed, SEED_LENGTH/2, seed.length);
		}

		public byte[] getSeed() {
			return seed;
		}
	}

    private static final int ITERATIONS = 128 * 8;
    private static final int KEY_LENGTH = 128;
    
    private final Cipher cipher;
    private final SecretKey key;
    private final Seed seed;
    
    /**
     * Init the AES mechanism with the password (used to generate PBE key) and random seed
     * @throws NoSuchPaddingException 
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeySpecException 
     */
    public AesSymmetricKeyEncrypter(String password, Seed seed) throws Exception {
    	this.seed = seed;
    	this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    	
        KeySpec spec = new PBEKeySpec(password.toCharArray(), seed.getSalt(), ITERATIONS, KEY_LENGTH);
        SecretKey secretKey = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(spec);
        this.key = new SecretKeySpec(secretKey.getEncoded(), "AES");       
    }

    /**
     * Encrypt the data and convert them to Base64 string
     */
    public String encrypt(String data) throws EmailSecurityException {
        try {
			cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(seed.getInitializationVector()));
			
	        return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
		} catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			throw new EmailSecurityException("An exception occured durind encryption of data", e);
		}
    }

    /**
     * Decrypt the data encoded by Base64
     */
    public String decrypt(String data) throws EmailSecurityException {
        try {
	        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(seed.getInitializationVector()));
	
	        return new String(cipher.doFinal(Base64.getDecoder().decode(data)));
		} catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			throw new EmailSecurityException("An exception occured durind decryption of data", e);
		}
    }
    
    /**
     * Return encoded seed by Base64 
     */
	public String getEncodedSeed() {
		return Base64.getEncoder().encodeToString(seed.getSeed());
	}
}
