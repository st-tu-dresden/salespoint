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
package org.salespointframework.useraccount;

import java.util.Optional;

import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.salespointframework.useraccount.UserAccount.UserAccountIdentifier;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

/**
 * Central service to manager {@link UserAccount} instances.
 *
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Service
public interface UserAccountManagement {

	/**
	 * Creates a new {@link UserAccount} from the given username, {@link UnencryptedPassword} and roles.
	 *
	 * @param userName the unique name of the user, the name is also used as login name.
	 * @param password the unencrypted password which will be encrypted.
	 * @param roles zero or more roles.
	 * @return a {@link UserAccount}, will never be {@literal null}.
	 */
	UserAccount create(String userName, UnencryptedPassword password, Role... roles);

	/**
	 * Creates a new {@link UserAccount} from the given username, {@link UnencryptedPassword} and roles.
	 *
	 * @param userName the unique name of the user, the name is also used as login name.
	 * @param password the unencrypted password which will be encrypted.
	 * @param roles zero or more roles.
	 * @return a {@link UserAccount}, will never be {@literal null}.
	 * @since 7.5
	 */
	UserAccount create(String userName, UnencryptedPassword password, Iterable<Role> roles);

	/**
	 * Creates a new {@link UserAccount} from the given username, {@link UnencryptedPassword}, email address and roles.
	 *
	 * @param userName the unique name of the user, the name is also used as login name.
	 * @param password the unencrypted password which will be encrypted.
	 * @param emailAddress the email address to be used.
	 * @param roles zero or more roles.
	 * @return a {@link UserAccount}, will never be {@literal null}.
	 * @since 7.1
	 */
	UserAccount create(String userName, UnencryptedPassword password, String emailAddress, Role... roles);

	/**
	 * Creates a new {@link UserAccount} from the given username, {@link UnencryptedPassword}, email address and roles.
	 *
	 * @param userName the unique name of the user, the name is also used as login name.
	 * @param password the unencrypted password which will be encrypted.
	 * @param emailAddress the email address to be used.
	 * @param roles zero or more roles.
	 * @return a {@link UserAccount}, will never be {@literal null}.
	 * @since 7.5
	 */
	UserAccount create(String userName, UnencryptedPassword password, String emailAddress, Iterable<Role> roles);

	/**
	 * Returns an {@link UserAccount} for a given identifier.
	 *
	 * @param userAccountIdentifier
	 * @return will never be {@literal null}.
	 */
	Optional<UserAccount> get(UserAccountIdentifier userAccountIdentifier);

	/**
	 * Saves the {@link UserAccount}
	 *
	 * @param userAccount
	 * @return will never be {@literal null}.
	 */
	UserAccount save(UserAccount userAccount);

	/**
	 * Enables the {@link UserAccount}.
	 *
	 * @param userAccountIdentifier
	 */
	void enable(UserAccountIdentifier userAccountIdentifier);

	/**
	 * Disables the {@link UserAccount}.
	 *
	 * @param userAccountIdentifier
	 */
	void disable(UserAccountIdentifier userAccountIdentifier);

	/**
	 * Changes the password of the {@link UserAccount}.
	 *
	 * @param userAccount
	 * @param password must not be {@literal null}.
	 */
	void changePassword(UserAccount userAccount, UnencryptedPassword password);

	/**
	 * Checks if an {@link UserAccount} exists.
	 *
	 * @param userAccountIdentifier
	 * @return whether a {@link UserAccount} with the given identifier exists.
	 */
	boolean contains(UserAccountIdentifier userAccountIdentifier);

	/**
	 * Finds all {@link UserAccount}s.
	 *
	 * @return will never be {@literal null}.
	 */
	Streamable<UserAccount> findAll();

	/**
	 * Finds only enabled {@link UserAccount}s.
	 *
	 * @return will never be {@literal null}.
	 */
	Streamable<UserAccount> findEnabled();

	/**
	 * Finds only disabled {@link UserAccount}s.
	 *
	 * @return will never be {@literal null}.
	 */
	Streamable<UserAccount> findDisabled();

	/**
	 * Returns the user with the given user name.
	 *
	 * @param username must not be {@literal null} or empty.
	 * @return will never be {@literal null}.
	 */
	Optional<UserAccount> findByUsername(String username);

	/**
	 * Deletes the given {@link UserAccount}. Note, that other aggregates that keep references to a {@link UserAccount}
	 * have to be deleted before a {@link UserAccount} can be deleted in the first place.
	 *
	 * @param account must not be {@literal null}.
	 * @return the deleted {@link UserAccount}
	 * @since 7.1
	 */
	UserAccount delete(UserAccount account);
}
