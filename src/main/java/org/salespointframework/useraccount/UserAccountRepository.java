package org.salespointframework.useraccount;

import org.salespointframework.core.SalespointRepository;

/**
 * Repository to persist {@link UserAccount} instances.
 * 
 * @author Oliver Gierke
 */
interface UserAccountRepository extends SalespointRepository<UserAccount, UserAccountIdentifier> {

	/**
	 * Returns all enabled {@link UserAccount}s.
	 * 
	 * @return
	 */
	Iterable<UserAccount> findByEnabledTrue();

	/**
	 * Returns all disabled {@link UserAccount}s.
	 * 
	 * @return
	 */
	Iterable<UserAccount> findByEnabledFalse();
}
