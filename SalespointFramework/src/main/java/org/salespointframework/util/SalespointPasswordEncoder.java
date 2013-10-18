package org.salespointframework.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Encodes and matches/verifies Passwords 
 * {@link BCryptPasswordEncoder} is used internally
 * 
 * @author Paul Henke
 *
 */
public class SalespointPasswordEncoder implements PasswordEncoder {

	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@Override
	public String encode(CharSequence rawPassword)  {
		return passwordEncoder.encode(rawPassword);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword)  {
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}
}
