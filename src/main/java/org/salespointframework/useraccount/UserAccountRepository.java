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

import org.salespointframework.core.SalespointRepository;
import org.salespointframework.useraccount.UserAccount.UserAccountIdentifier;
import org.springframework.data.util.Streamable;

/**
 * Repository to persist {@link UserAccount} instances.
 *
 * @author Oliver Gierke
 */
interface UserAccountRepository
		extends SalespointRepository<UserAccount, UserAccountIdentifier>, DetachingRepository<UserAccount> {

	/**
	 * Returns all enabled {@link UserAccount}s.
	 *
	 * @return
	 */
	Streamable<UserAccount> findByEnabledTrue();

	/**
	 * Returns all disabled {@link UserAccount}s.
	 *
	 * @return
	 */
	Streamable<UserAccount> findByEnabledFalse();

	/**
	 * Returns the {@link UserAccount} with the given email address.
	 *
	 * @param emailAddress must not be {@literal null}.
	 * @return
	 * @since 7.1
	 */
	Optional<UserAccount> findByEmail(String emailAddress);

	/**
	 * Returns the {@link UserAccount} for the given username, if one exists.
	 *
	 * @param username must not be {@literal null}.
	 * @return will never be {@literal null}.
	 * @since 9.0
	 */
	Optional<UserAccount> findByUsername(String username);

	/**
	 * Saves the given {@link UserAccount} and immediately flushes the persistence context.
	 *
	 * @param <S> the type of {@link UserAccount}.
	 * @param entity must not be {@literal null}.
	 * @return will never be {@literal null}.
	 * @since 9.0
	 */
	<S extends UserAccount> S saveAndFlush(S entity);
}
