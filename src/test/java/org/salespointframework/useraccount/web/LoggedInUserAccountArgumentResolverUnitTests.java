/*
 * Copyright 2017-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.salespointframework.useraccount.web;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.salespointframework.useraccount.AuthenticationManagement;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.core.MethodParameter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.ServletRequestBindingException;

/**
 * Unit tests for {@link LoggedInUserAccountArgumentResolver}.
 *
 * @author Oliver Gierke
 */
@ExtendWith(MockitoExtension.class)
class LoggedInUserAccountArgumentResolverUnitTests {

	@Mock AuthenticationManagement authenticationManager;
	@Mock UserAccount account;

	LoggedInUserAccountArgumentResolver resolver;

	@BeforeEach
	void setUp() {
		resolver = new LoggedInUserAccountArgumentResolver(authenticationManager);
	}

	@Test // #37
	void supportsOptionalOfUserAccountAsMethodArgumentType() throws Exception {

		Method method = Sample.class.getMethod("valid", Optional.class);
		assertThat(resolver.supportsParameter(new MethodParameter(method, 0)), is(true));
	}

	@Test // #37
	void rejectsNonAnnotatedOptionalOfUserAccountAsMethodArgumentType() throws Exception {

		Method method = Sample.class.getMethod("notAnnotated", Optional.class);
		assertThat(resolver.supportsParameter(new MethodParameter(method, 0)), is(false));
	}

	@Test // #37
	void rejectsNonOptionalWrappedUserAccountAsMethodArgumentType() throws Exception {

		Method method = Sample.class.getMethod("notAnnotated", Optional.class);
		assertThat(resolver.supportsParameter(new MethodParameter(method, 0)), is(false));
	}

	@Test // #37
	void returnsUserAccountProvidedByAuthenticationManager() throws Exception {

		Method method = Sample.class.getMethod("valid", Optional.class);
		when(authenticationManager.getCurrentUser()).thenReturn(Optional.empty());
		assertThat(resolver.resolveArgument(new MethodParameter(method, 0), null, null, null), is(Optional.empty()));
	}

	@Test
	void supportsAnnotatedUserAccount() throws Exception {

		Method method = Sample.class.getMethod("noOptional", UserAccount.class);
		when(authenticationManager.getCurrentUser()).thenReturn(Optional.of(account));

		assertThat(resolver.supportsParameter(new MethodParameter(method, 0)), is(true));
		assertThat(resolver.resolveArgument(new MethodParameter(method, 0), null, null, null), is(account));
	}

	@Test
	void throwsExceptionIfNoUserAccountAvailableButRequired() throws Exception {

		Method method = Sample.class.getMethod("noOptional", UserAccount.class);
		when(authenticationManager.getCurrentUser()).thenReturn(Optional.empty());

		assertThatExceptionOfType(ServletRequestBindingException.class) //
				.isThrownBy(() -> resolver.resolveArgument(new MethodParameter(method, 0), null, null, null)) //
				.withMessageContaining("Optional<UserAccount>");
	}

	@Test // #389, #337
	void returnsNullForGuardedNoOptional() throws Exception {

		Method method = Sample.class.getMethod("guardedNoOptional", UserAccount.class);
		when(authenticationManager.getCurrentUser()).thenReturn(Optional.empty());

		assertThat(resolver.resolveArgument(new MethodParameter(method, 0), null, null, null)).isNull();
	}

	static interface Sample {

		void valid(@LoggedIn Optional<UserAccount> account);

		void notAnnotated(Optional<UserAccount> account);

		void noOptional(@LoggedIn UserAccount account);

		@PreAuthorize("expression")
		void guardedNoOptional(@LoggedIn UserAccount account);
	}
}
