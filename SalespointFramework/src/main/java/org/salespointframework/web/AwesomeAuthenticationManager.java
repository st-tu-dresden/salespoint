package org.salespointframework.web;

import org.salespointframework.core.user.User;
import org.salespointframework.core.user.UserIdentifier;
import org.salespointframework.core.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

// http://docs.spring.io/spring-security/site/docs/3.2.x/reference/html/technical-overview.html
public class AwesomeAuthenticationManager implements AuthenticationManager {

	@Autowired
	private UserManager userManager;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		
		UserIdentifier userIdentifier = new UserIdentifier(userDetails.getUsername());
		User user = userManager.get(User.class, userIdentifier);

		if(user.verifyPassword(userDetails.getPassword())) {
			return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
		}
		throw new BadCredentialsException("Bad Credentials");
	}
	
	
	// TODO Nötig?
	 public void logout(){
	        SecurityContextHolder.clearContext();
    }

}
