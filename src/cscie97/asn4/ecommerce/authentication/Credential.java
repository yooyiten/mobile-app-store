package cscie97.asn4.ecommerce.authentication;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


/**
 * {@code Credential} represents username and password credentials that can
 * be associated with a Mobile Application Store user and are required for
 * the login process.
 * 
 * Password hashing implementation adapted from http://jerryorr.blogspot.com/2012/05/secure-password-storage-lots-of-donts.html
 */
public class Credential {

	private final String userId;
	private final String userName;
	private byte[] password;
	private byte[] salt;
	
	protected Credential(String userId, String userName, String password) 
			  throws UnsupportedEncodingException, NoSuchAlgorithmException, 
			  		 InvalidKeySpecException{
		this.userId = userId;
		this.userName= userName;
		this.salt = generateSalt();
		this.password = hashPassword(password);
	}
	
	/**
	 * Generates salt for the hashing.
	 * 
	 * @return salt for hashing
	 * @throws NoSuchAlgorithmException if salting algorithm is invalid
	 */
	private byte[] generateSalt() throws NoSuchAlgorithmException{
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		this.salt = new byte[8];
		random.nextBytes(salt);
		return salt;
	}
	
	/**
	 * Hashes a String password using the PBKDF2 algorithm.
	 * 
	 * @param password	password to be hashed
	 * @return	hashed password
	 * @throws NoSuchAlgorithmException if hashing algorithm is invalid
	 * @throws InvalidKeySpecException	if key spec is invalid
	 */
	private byte[] hashPassword(String password) 
			  throws NoSuchAlgorithmException, InvalidKeySpecException{
		String algorithm = "PBKDF2WithHmacSHA1";
		int derivedKeyLength = 160;
		int iterations = 20000;
		KeySpec spec = new PBEKeySpec(password.toCharArray(), this.salt, iterations, 
									  derivedKeyLength);
		SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);
		return f.generateSecret(spec).getEncoded();		
	}
	
	/**
	 * Checks if a password matches the Credential's password.
	 * 
	 * @param password	password to be compared to stored password
	 * @return	true if passwords match, false if not
	 * @throws NoSuchAlgorithmException	if hashing algorithm is invalid
	 * @throws InvalidKeySpecException	if key spec is invalid
	 */
	public boolean checkPassword(String pw) 
			  throws NoSuchAlgorithmException, InvalidKeySpecException{
		byte[] pwLogin = hashPassword(pw);
		return Arrays.equals(this.password, pwLogin);
	}

	public String getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}
}
