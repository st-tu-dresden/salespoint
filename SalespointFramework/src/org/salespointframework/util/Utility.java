package org.salespointframework.util;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

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
		SecureRandom sr = new SecureRandom();
		byte[] salt = new byte[8];
		sr.nextBytes(salt);
		return hashPassword(password, salt);
	}
	
	
	// SCHEISS CHECKED EXCEPTIONS!!!!!!!!!!11111111elf
	public static String hashPassword(String password, byte[] salt) {
		
		byte[] passBytes = null;
		byte[] digest = null ;

		try {
			passBytes = password.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unsupported (getBytes)");
		}
		
		try {
			MessageDigest m = MessageDigest.getInstance("SHA-256");
			
			m.update(salt);
			m.update(passBytes);
			
			digest = m.digest();
			
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Unsupported (getInstance)");
		}
		
		String saltString = DatatypeConverter.printBase64Binary(salt);
		String hashed = new BigInteger(1, digest).toString(16);
		
		return saltString + hashed;
		
	}
	
	public static boolean verifyPassword(String password, String hashedPassword) 
	{
		String saltString = hashedPassword.substring(0, 12);
		byte[] salt = DatatypeConverter.parseBase64Binary(saltString);
		String newHashedPassword = hashPassword(password, salt);
		
		return newHashedPassword.equals(hashedPassword);
	}
	
}
