package org.salespointframework.useraccount;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.salespointframework.useraccount.UserAccountRepositoryIntegrationTests.*;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.salespointframework.useraccount.UserAccountDetailService.UserAccountDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Units tests for {@link SpringSecurityAuthenticationManager}.
 *
 * @author Oliver Gierke
 */
@RunWith(MockitoJUnitRunner.class)
public class SpringSecurityAuthenticationManagerUnitTests {

	SpringSecurityAuthenticationManager authenticationManager;
	@Mock UserAccountRepository repository;
	@Mock PasswordEncoder passwordEncoder;
	UserAccount account;
	
	@Before
	public void setUp() {
		
		this.account = createAccount();
		when(repository.findOne(account.getIdentifier())).thenReturn(Optional.of(account));
		
		this.authenticationManager = new SpringSecurityAuthenticationManager(repository, passwordEncoder);
	}
	
	@After
	public void resetAuthentication() {
		SecurityContextHolder.getContext().setAuthentication(null);
	}
	
	@Test
	public void returnsNullIfNoUserIsAuthenticated() {
		assertThat(authenticationManager.getCurrentUser(), is(nullValue()));
	}
	
	@Test
	public void returnsCurrentlyAuthenticatedUser() {
		
		authenticate(account);
		
		Optional<UserAccount> currentUser = authenticationManager.getCurrentUser();
		
		assertThat(currentUser.isPresent(), is(true));
		assertThat(currentUser.get(), is(account));
	}
	
	@Test
	public void delegatesPasswordMatchCorrectly() {
		
		Password existing = new Password("password", true);
		Password matching = new Password("password");
		Password failing = new Password("failing");
		
		when(passwordEncoder.matches("password", "password")).thenReturn(true);
		when(passwordEncoder.matches("password", "failing")).thenReturn(false);
		
		assertThat(authenticationManager.matches(matching, existing), is(true));
		assertThat(authenticationManager.matches(failing, existing), is(false));
		assertThat(authenticationManager.matches(null, existing), is(false));
	}
	
	
	private static void authenticate(UserAccount account) {
		
		UserAccountDetails accountDetails = new UserAccountDetails(account);
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(accountDetails, account, accountDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
