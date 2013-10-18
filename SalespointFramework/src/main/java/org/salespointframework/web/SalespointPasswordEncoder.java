package org.salespointframework.web;

import org.salespointframework.util.Passwords;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SalespointPasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword)  {
		return Passwords.hash(rawPassword.toString());
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword)  {
		return Passwords.verify(rawPassword.toString(), encodedPassword.toString());
	}
}
