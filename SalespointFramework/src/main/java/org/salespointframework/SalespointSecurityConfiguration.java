package org.salespointframework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Basic salespoint security configuration setting up the {@link AuthenticationManagerBuilder} to work with the
 * {@link UserDetailsService} implementaiton as well as the {@link PasswordEncoder} we provide.
 * 
 * TODO: Pull {@link EnableGlobalMethodSecurity} in here as well as soon as it is supported on superclasses. 
 * 
 * @author Oliver Gierke
 */
@EnableWebSecurity
public class SalespointSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired UserDetailsService userDetailsService;
	@Autowired PasswordEncoder passwordEncoder;

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder)
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder amBuilder) throws Exception {
		amBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	/**
	 * Overridden to expose the {@link AuthenticationManager} as a Spring bean.
	 * 
	 * @see WebSecurityConfigurerAdapter#authenticationManagerBean()
	 */
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	/**
	 * Allow anonymous access to resources by default.
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**");
	}
}
