/*
 * Copyright 2017 the original author or authors.
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
package org.salespointframework.useraccount.web;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.salespointframework.useraccount.AuthenticationManager;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.ServletRequestBindingException;

/**
 * Unit tests for {@link LoggedInUserAccountArgumentResolver}.
 * 
 * @author Oliver Gierke
 */
@RunWith(MockitoJUnitRunner.class)
public class LoggedInUserAccountArgumentResolverUnitTests {

	@Mock AuthenticationManager authenticationManager;
	@Mock UserAccount account;

	LoggedInUserAccountArgumentResolver resolver;

	public @Rule ExpectedException exception = ExpectedException.none();

	@Before
	public void setUp() {
		resolver = new LoggedInUserAccountArgumentResolver(authenticationManager);
	}

	/**
	 * @see #37
	 */
	@Test
	public void supportsOptionalOfUserAccountAsMethodArgumentType() throws Exception {

		Method method = Sample.class.getMethod("valid", Optional.class);
		assertThat(resolver.supportsParameter(new MethodParameter(method, 0)), is(true));
	}

	/**
	 * @see #37
	 */
	@Test
	public void rejectsNonAnnotatedOptionalOfUserAccountAsMethodArgumentType() throws Exception {

		Method method = Sample.class.getMethod("notAnnotated", Optional.class);
		assertThat(resolver.supportsParameter(new MethodParameter(method, 0)), is(false));
	}

	/**
	 * @see #37
	 */
	@Test
	public void rejectsNonOptionalWrappedUserAccountAsMethodArgumentType() throws Exception {

		Method method = Sample.class.getMethod("notAnnotated", Optional.class);
		assertThat(resolver.supportsParameter(new MethodParameter(method, 0)), is(false));
	}

	/**
	 * @see #37
	 */
	@Test
	public void returnsUserAccountProvidedByAuthenticationManager() throws Exception {

		Method method = Sample.class.getMethod("valid", Optional.class);
		when(authenticationManager.getCurrentUser()).thenReturn(Optional.empty());
		assertThat(resolver.resolveArgument(new MethodParameter(method, 0), null, null, null), is(Optional.empty()));
	}

	@Test
	public void supportsAnnotatedUserAccount() throws Exception {

		Method method = Sample.class.getMethod("noOptional", UserAccount.class);
		when(authenticationManager.getCurrentUser()).thenReturn(Optional.of(account));

		assertThat(resolver.supportsParameter(new MethodParameter(method, 0)), is(true));
		assertThat(resolver.resolveArgument(new MethodParameter(method, 0), null, null, null), is(account));
	}

	@Test
	public void throwsExceptionIfNoUserAccountAvailableButRequired() throws Exception {

		Method method = Sample.class.getMethod("noOptional", UserAccount.class);
		when(authenticationManager.getCurrentUser()).thenReturn(Optional.empty());

		exception.expect(ServletRequestBindingException.class);
		exception.expectMessage("Optional<UserAccount>");

		assertThat(resolver.resolveArgument(new MethodParameter(method, 0), null, null, null), is(account));
	}

	static interface Sample {

		void valid(@LoggedIn Optional<UserAccount> account);

		void notAnnotated(Optional<UserAccount> account);

		void noOptional(@LoggedIn UserAccount account);
	}
}
