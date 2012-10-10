package org.salespointframework.util;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;

import javax.xml.bind.DatatypeConverter;


//http://docs.oracle.com/javase/1.5.0/docs/guide/security/CryptoSpec.html#AppA

/**
 * Utility class
 * 
 * @author Paul Henke
 *
 */
public class Utility {

	public static String hashPassword(String password) {
		Objects.requireNonNull(password, "password must not be null");
		SecureRandom sr = new SecureRandom();
		byte[] salt = new byte[8];
		sr.nextBytes(salt);
		return hashPassword(password, salt);
	}
	
	
	// SCHEISS CHECKED EXCEPTIONS!!!!!!!!!!11111111elf
	private static String hashPassword(String password, byte[] salt) {
		Objects.requireNonNull(password, "password must not be null");
		Objects.requireNonNull(salt, "salt must not be null");
		
		byte[] passBytes = null;
		byte[] digest = null ;

		try {
			passBytes = password.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("getBytes(UTF-8) is unsupported");
		}
		
		try {
			MessageDigest m = MessageDigest.getInstance("SHA-256");
			
			m.update(salt);
			m.update(passBytes);
			
			digest = m.digest();
			
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("MessageDigest.getInstance(\"SHA-256\") is unsupported");
		}
		
		String saltString = DatatypeConverter.printBase64Binary(salt);
		String hashed = new BigInteger(1, digest).toString(16);
		
		return saltString + hashed;
		
	}
	
	public static boolean verifyPassword(String password, String hashedPassword) 
	{
		Objects.requireNonNull(password, "password must not be null");
		Objects.requireNonNull(hashedPassword, "hashedPassword must not be null");
		
		String saltString = hashedPassword.substring(0, 12);
		byte[] salt = DatatypeConverter.parseBase64Binary(saltString);
		String newHashedPassword = hashPassword(password, salt);
		
		return newHashedPassword.equals(hashedPassword);
	}
	
}
