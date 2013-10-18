package org.salespointframework.web;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SalespointDaoAuthenticationProvider extends DaoAuthenticationProvider {
	
	public SalespointDaoAuthenticationProvider() {
		this.setPasswordEncoder(new SalespointPasswordEncoder());
		this.setUserDetailsService(new SalespointUserDetailService());
		
		//TODO
		// auch im User umstellen
		//this.setPasswordEncoder(new BCryptPasswordEncoder());
	}
	

}
