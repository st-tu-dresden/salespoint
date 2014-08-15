package org.salespointframework.useraccount;

import java.util.Optional;

/**
 * Application component for authentication related use cases.
 * 
 * @author Oliver Gierke
 */
public interface AuthenticationManager {

	/**
	 * Returns the {@link UserAccount} of the currently logged in user or {@link Optional#empty()} if no-one is currently
	 * logged in.
	 * 
	 * @return
	 */
	Optional<UserAccount> getCurrentUser();

	/**
	 * Returns whether the given candidate {@link Password} matches the given existing one.
	 * 
	 * @param candidate can be {@literal null}.
	 * @param existing must not be {@literal null}.
	 * @return
	 */
	boolean matches(Password candidate, Password existing);
}
