/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.salespointframework.core.useraccount;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.salespointframework.core.useraccount.UserAccountRepositoryIntegrationTests.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.salespointframework.core.useraccount.UserAccountDetailService.UserAccountDetails;
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
public class SpringSecrurityAuthenticationManagerUnitTests {

	SpringSecurityAuthenticationManager authenticationManager;
	@Mock UserAccountRepository repository;
	@Mock PasswordEncoder passwordEncoder;
	UserAccount account;
	
	@Before
	public void setUp() {
		
		this.account = createAccount();
		when(repository.findOne(account.getIdentifier())).thenReturn(account);
		
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
		assertThat(authenticationManager.getCurrentUser(), is(account));
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
