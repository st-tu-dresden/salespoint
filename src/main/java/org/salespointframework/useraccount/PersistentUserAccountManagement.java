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

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.salespointframework.useraccount.Password.EncryptedPassword;
import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.salespointframework.useraccount.UserAccount.UserAccountIdentifier;
import org.springframework.data.util.Streamable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Implementation of {@link UserAccountManagement} using a {@link UserAccountRepository} to persist {@link UserAccount}
 * instances. It also manages password encryption using the configured {@link PasswordEncoder}.
 *
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class PersistentUserAccountManagement implements UserAccountManagement {

	private static final String EMAIL_PLACEHOLDER = "¯\\_(ツ)_/¯";

	private final @NonNull UserAccountRepository repository;
	private final @NonNull PasswordEncoder passwordEncoder;
	private final @NonNull AuthenticationProperties config;

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManagement#create(java.lang.String, org.salespointframework.useraccount.Password.UnencryptedPassword, org.salespointframework.useraccount.Role[])
	 */
	@Override
	@Transactional
	public UserAccount create(String userName, UnencryptedPassword password, Role... roles) {
		return create(userName, password, List.of(roles));
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManagement#create(java.lang.String, org.salespointframework.useraccount.Password.UnencryptedPassword, java.lang.Iterable)
	 */
	@Override
	@Transactional
	public UserAccount create(String userName, UnencryptedPassword password, Iterable<Role> roles) {
		return create(userName, password, EMAIL_PLACEHOLDER, roles);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManagement#create(java.lang.String, org.salespointframework.useraccount.Password.UnencryptedPassword, java.lang.String, org.salespointframework.useraccount.Role[])
	 */
	@Override
	@Transactional
	public UserAccount create(String userName, UnencryptedPassword password, String emailAddress, Role... roles) {
		return create(userName, password, emailAddress, List.of(roles));
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManagement#create(java.lang.String, org.salespointframework.useraccount.Password.UnencryptedPassword, java.lang.String, java.lang.Iterable)
	 */
	@Override
	@Transactional
	public UserAccount create(String userName, UnencryptedPassword password, String emailAddress, Iterable<Role> roles) {

		Assert.hasText(userName, "Username must not be null or empty!");
		Assert.notNull(password, "Password must not be null!");
		Assert.hasText(emailAddress, "Email address must not be null or empty!");
		Assert.notNull(roles, "Roles must not be null!");

		// Reject username if a user with that name already exists
		findByUsername(userName).ifPresent(user -> {
			throw new IllegalArgumentException(String.format("User with name %s already exists!", userName));
		});

		var encryptedPassword = encrypt(password);
		var account = new UserAccount(UserAccountIdentifier.of(userName), encryptedPassword, roles);
		account.setEmail(EMAIL_PLACEHOLDER.equals(emailAddress) ? null : emailAddress);

		return save(account);

	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManagement#get(org.salespointframework.useraccount.UserAccountIdentifier)
	 */
	@Override
	public Optional<UserAccount> get(UserAccountIdentifier userAccountIdentifier) {

		Assert.notNull(userAccountIdentifier, "userAccountIdentifier must not be null");
		return repository.findById(userAccountIdentifier);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManagement#save(org.salespointframework.useraccount.UserAccount)
	 */
	@Override
	@Transactional
	public UserAccount save(UserAccount userAccount) {

		Assert.notNull(userAccount, "UserAccount must not be null!");

		if (config.isLoginViaEmail()) {
			Assert.hasText(userAccount.getEmail(),
					"Email address must not be null or empty if login via email is configured!");
		}

		// Reject if there's already a different user with the same email address
		Optional.ofNullable(userAccount.getEmail()) //
				.flatMap(repository::findByEmail) //
				.filter(it -> !it.equals(userAccount)) //
				.ifPresent(it -> {
					throw new IllegalArgumentException(
							String.format("A different UserAccount with email %s already exists!", it.getEmail()));
				});

		return repository.save(userAccount);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManagement#enable(org.salespointframework.useraccount.UserAccountIdentifier)
	 */
	@Override
	@Transactional
	public void enable(UserAccountIdentifier userAccountIdentifier) {

		Assert.notNull(userAccountIdentifier, "UserAccountIdentifier must not be null!");

		get(userAccountIdentifier).ifPresent(account -> account.setEnabled(true));
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManagement#disable(org.salespointframework.useraccount.UserAccountIdentifier)
	 */
	@Override
	@Transactional
	public void disable(UserAccountIdentifier userAccountIdentifier) {

		Assert.notNull(userAccountIdentifier, "UserAccountIdentifier must not be null!");

		get(userAccountIdentifier).ifPresent(account -> account.setEnabled(false));
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManagement#changePassword(org.salespointframework.useraccount.UserAccount, org.salespointframework.useraccount.Password.UnencryptedPassword)
	 */
	@Override
	@Transactional
	public void changePassword(UserAccount userAccount, UnencryptedPassword password) {

		Assert.notNull(userAccount, "userAccount must not be null");
		Assert.notNull(password, "password must not be null");

		userAccount.setPassword(encrypt(password));
		save(userAccount);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManagement#contains(org.salespointframework.useraccount.UserAccountIdentifier)
	 */
	@Override
	public boolean contains(UserAccountIdentifier userAccountIdentifier) {

		Assert.notNull(userAccountIdentifier, "userAccountIdentifier must not be null");
		return repository.existsById(userAccountIdentifier);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManagement#findAll()
	 */
	@Override
	public Streamable<UserAccount> findAll() {
		return repository.findAll();
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManagement#findEnabled()
	 */
	@Override
	public Streamable<UserAccount> findEnabled() {
		return repository.findByEnabledTrue();
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManagement#findDisabled()
	 */
	@Override
	public Streamable<UserAccount> findDisabled() {
		return repository.findByEnabledFalse();
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManagement#findByUsername(java.lang.String)
	 */
	@Override
	public Optional<UserAccount> findByUsername(String username) {

		Assert.hasText(username, "Username must not be null or empty!");
		return repository.findById(UserAccountIdentifier.of(username));
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManagement#delete(org.salespointframework.useraccount.UserAccount)
	 */
	@Override
	@Transactional
	public UserAccount delete(UserAccount account) {

		Assert.notNull(account, "UserAccount must not be null!");

		repository.delete(account);

		return account;
	}

	/**
	 * Encrypts the given raw password value.
	 *
	 * @param password must not be {@literal null}.
	 * @return
	 */
	private EncryptedPassword encrypt(UnencryptedPassword password) {

		Assert.notNull(password, "Password must not be null!");

		return EncryptedPassword.of(passwordEncoder.encode(password.asString()));
	}
}
