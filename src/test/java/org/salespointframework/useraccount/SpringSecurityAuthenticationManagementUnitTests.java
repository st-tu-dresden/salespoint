/*
 * Copyright 2017-2022 the original author or authors.
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.salespointframework.useraccount.Password.EncryptedPassword;
import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.salespointframework.useraccount.SpringSecurityAuthenticationManagement.UserAccountDetails;
import org.salespointframework.useraccount.UserAccount.UserAccountIdentifier;
import org.springframework.data.util.Streamable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Units tests for {@link SpringSecurityAuthenticationManagement}.
 *
 * @author Oliver Gierke
 */
@ExtendWith(MockitoExtension.class)
class SpringSecurityAuthenticationManagementUnitTests {

	SpringSecurityAuthenticationManagement authenticationManager;

	@Mock UserAccountRepository repository;
	@Mock PasswordEncoder passwordEncoder;
	AuthenticationProperties config = new AuthenticationProperties(false);
	SessionRegistry sessions = new SessionRegistryImpl();

	UserAccount account;

	@BeforeEach
	void setUp() {

		this.account = createAccount();
		this.authenticationManager = new SpringSecurityAuthenticationManagement(repository, passwordEncoder, config,
				sessions);
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

		var currentUser = authenticationManager.getCurrentUser();

		assertThat(currentUser.isPresent(), is(true));
		assertThat(currentUser.get(), is(account));
	}

	@Test
	void delegatesPasswordMatchCorrectly() {

		var existing = EncryptedPassword.of("password");
		var matching = UnencryptedPassword.of("password");
		var failing = UnencryptedPassword.of("failing");

		when(passwordEncoder.matches("password", "password")).thenReturn(true);

		assertThat(authenticationManager.matches(matching, existing), is(true));
		assertThat(authenticationManager.matches(failing, existing), is(false));
		assertThat(authenticationManager.matches(null, existing), is(false));
	}

	@Test // #222
	void usesByEmailLookupIfConfigured() {

		var authenticationManager = new SpringSecurityAuthenticationManagement(repository,
				passwordEncoder, new AuthenticationProperties(true), sessions);

		doReturn(Optional.of(account)).when(repository).findByEmail(any());

		authenticationManager.loadUserByUsername("username");

		verify(repository).findByEmail(any());
		verify(repository, never()).findById(any());
	}

	@Test // #280
	void exposesRolesAsRoleUnderscorePrefixedAuthorities() {

		var customerRole = Role.of("CUSTOMER");
		var adminRole = Role.of("ROLE_ADMIN");

		var identifier = UserAccountIdentifier.of("4711");
		var userAccount = new UserAccount(identifier, EncryptedPassword.of("encrypted"), customerRole, adminRole);

		doReturn(Optional.of(userAccount)).when(repository).findById(identifier);

		var userDetails = authenticationManager.loadUserByUsername("4711");

		assertThat(userDetails.getAuthorities()) //
				.allMatch(it -> it.getAuthority().startsWith("ROLE_"));
	}

	@Test // #379
	void updatesAuthentication() {

		assertThat(authenticationManager.getCurrentUser()).isEmpty();

		var identifier = UserAccountIdentifier.of("4711");
		var account = new UserAccount(identifier, EncryptedPassword.of("encrypted"), Role.of("ADMIN"));

		doReturn(Optional.of(account)).when(repository).findById(identifier);

		authenticationManager.updateAuthentication(account);

		assertThat(authenticationManager.getCurrentUser()).hasValue(account);
	}

	@Test // #423
	void doesNotReturnAnyLoggedInUsersIfNoSessionsAvailable() {

		doReturn(Streamable.empty()).when(repository).findAllById(Collections.emptyList());

		assertThat(authenticationManager.getCurrentlyLoggedInUserAccounts()).isEmpty();
	}

	@Test // #423
	void returnsLoggedInUserAccountForRegisteredSession() {

		var account = UserAccountTestUtils.createUserAccount();
		sessions.registerNewSession("someId", new UserAccountDetails(account));

		doReturn(Streamable.of(account)).when(repository).findAllById(List.of(account.getId()));

		assertThat(authenticationManager.getCurrentlyLoggedInUserAccounts()).containsExactly(account);
	}

	private static void authenticate(UserAccount account) {

		var details = new UserAccountDetails(account);
		var authentication = new UsernamePasswordAuthenticationToken(details, account, details.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
