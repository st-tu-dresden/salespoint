package org.salespointframework.useraccount.web;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.salespointframework.useraccount.AuthenticationManager;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.core.MethodParameter;

/**
 * Unit tests for {@link LoggedInUserAccountArgumentResolver}.
 * 
 * @author Oliver Gierke
 */
@RunWith(MockitoJUnitRunner.class)
public class LoggedInUserAccountArgumentResolverUnitTests {

	@Mock AuthenticationManager authenticationManager;

	LoggedInUserAccountArgumentResolver resolver;

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

		when(authenticationManager.getCurrentUser()).thenReturn(Optional.empty());
		assertThat(resolver.resolveArgument(null, null, null, null), is(Optional.empty()));
	}

	static interface Sample {

		void valid(@LoggedIn Optional<UserAccount> account);

		void notAnnotated(Optional<UserAccount> account);

		void noOptional(@LoggedIn UserAccount account);
	}
}
