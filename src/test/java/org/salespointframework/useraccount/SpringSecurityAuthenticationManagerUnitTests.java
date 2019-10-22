/*
 * Copyright 2017-2019 the original author or authors.
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
package org.salespointframework.useraccount;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.salespointframework.useraccount.UserAccountRepositoryIntegrationTests.*;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.salespointframework.useraccount.Password.EncryptedPassword;
import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.salespointframework.useraccount.SpringSecurityAuthenticationManager.UserAccountDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Units tests for {@link SpringSecurityAuthenticationManager}.
 *
 * @author Oliver Gierke
 */
@ExtendWith(MockitoExtension.class)
class SpringSecurityAuthenticationManagerUnitTests {

	SpringSecurityAuthenticationManager authenticationManager;

	@Mock UserAccountRepository repository;
	@Mock PasswordEncoder passwordEncoder;
	AuthenticationProperties config = new AuthenticationProperties(false);

	UserAccount account;

	@BeforeEach
	void setUp() {

		this.account = createAccount();
		this.authenticationManager = new SpringSecurityAuthenticationManager(repository, passwordEncoder, config);
	}

	@AfterEach
	void resetAuthentication() {
		SecurityContextHolder.getContext().setAuthentication(null);
	}

	@Test // #76
	void returnsOptionalEmptyIfNoUserIsAuthenticated() {
		assertThat(authenticationManager.getCurrentUser(), is(Optional.empty()));
	}

	@Test
	void returnsCurrentlyAuthenticatedUser() {

		authenticate(account);

		when(repository.findById(account.getId())).thenReturn(Optional.of(account));

		Optional<UserAccount> currentUser = authenticationManager.getCurrentUser();

		assertThat(currentUser.isPresent(), is(true));
		assertThat(currentUser.get(), is(account));
	}

	@Test
	void delegatesPasswordMatchCorrectly() {

		EncryptedPassword existing = EncryptedPassword.of("password");
		UnencryptedPassword matching = UnencryptedPassword.of("password");
		UnencryptedPassword failing = UnencryptedPassword.of("failing");

		when(passwordEncoder.matches("password", "password")).thenReturn(true);

		assertThat(authenticationManager.matches(matching, existing), is(true));
		assertThat(authenticationManager.matches(failing, existing), is(false));
		assertThat(authenticationManager.matches(null, existing), is(false));
	}

	@Test // #222
	void usesByEmailLookupIfConfigured() {

		SpringSecurityAuthenticationManager authenticationManager = new SpringSecurityAuthenticationManager(repository,
				passwordEncoder, new AuthenticationProperties(true));

		doReturn(Optional.of(account)).when(repository).findByEmail(any());

		authenticationManager.loadUserByUsername("username");

		verify(repository).findByEmail(any());
		verify(repository, never()).findById(any());
	}

	@Test // # 280
	void exposesRolesAsRoleUnderscorePrefixedAuthorities() {

		Role customerRole = Role.of("CUSTOMER");
		Role adminRole = Role.of("ROLE_ADMIN");

		UserAccountIdentifier identifier = new UserAccountIdentifier("4711");
		var userAccount = new UserAccount(identifier, EncryptedPassword.of("encrypted"), customerRole, adminRole);

		doReturn(Optional.of(userAccount)).when(repository).findById(identifier);

		var userDetails = authenticationManager.loadUserByUsername("4711");

		assertThat(userDetails.getAuthorities()) //
				.allMatch(it -> it.getAuthority().startsWith("ROLE_"));
	}

	private static void authenticate(UserAccount account) {

		UserAccountDetails accountDetails = new UserAccountDetails(account);

		Authentication authentication = new UsernamePasswordAuthenticationToken(accountDetails, account,
				accountDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
