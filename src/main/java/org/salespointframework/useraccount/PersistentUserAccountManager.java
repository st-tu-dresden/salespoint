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

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.salespointframework.useraccount.Password.EncryptedPassword;
import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.springframework.data.util.Streamable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Implementation of {@link UserAccountManager} using a {@link UserAccountRepository} to persist {@link UserAccount}
 * instances. It also manages password encryption using the configured {@link PasswordEncoder}.
 *
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class PersistentUserAccountManager implements UserAccountManager {

	private static final String EMAIL_PLACEHOLDER = "¯\\_(ツ)_/¯";

	private final @NonNull UserAccountRepository repository;
	private final @NonNull PasswordEncoder passwordEncoder;
	private final @NonNull AuthenticationProperties config;

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManager#create(java.lang.String, org.salespointframework.useraccount.Password.UnencryptedPassword, org.salespointframework.useraccount.Role[])
	 */
	@Override
	@Transactional
	public UserAccount create(String userName, UnencryptedPassword password, Role... roles) {
		return create(userName, password, EMAIL_PLACEHOLDER, roles);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManager#create(java.lang.String, org.salespointframework.useraccount.Password.UnencryptedPassword, java.lang.String, org.salespointframework.useraccount.Role[])
	 */
	@Override
	@Transactional
	public UserAccount create(String userName, UnencryptedPassword password, String emailAddress, Role... roles) {

		Assert.hasText(userName, "Username must not be null or empty!");
		Assert.notNull(password, "Password must not be null!");
		Assert.hasText(emailAddress, "Email address must not be null or empty!");
		Assert.notNull(roles, "Roles must not be null!");

		// Reject username if a user with that name already exists
		findByUsername(userName).ifPresent(user -> {
			throw new IllegalArgumentException(String.format("User with name %s already exists!", userName));
		});

		EncryptedPassword encryptedPassword = encrypt(password.asString());
		UserAccount account = new UserAccount(new UserAccountIdentifier(userName), encryptedPassword, roles);
		account.setEmail(EMAIL_PLACEHOLDER.equals(emailAddress) ? null : emailAddress);

		return save(account);

	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManager#get(org.salespointframework.useraccount.UserAccountIdentifier)
	 */
	@Override
	public Optional<UserAccount> get(UserAccountIdentifier userAccountIdentifier) {

		Assert.notNull(userAccountIdentifier, "userAccountIdentifier must not be null");
		return repository.findById(userAccountIdentifier);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManager#save(org.salespointframework.useraccount.UserAccount)
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
	 * @see org.salespointframework.useraccount.UserAccountManager#enable(org.salespointframework.useraccount.UserAccountIdentifier)
	 */
	@Override
	@Transactional
	public void enable(UserAccountIdentifier userAccountIdentifier) {

		Assert.notNull(userAccountIdentifier, "UserAccountIdentifier must not be null!");

		get(userAccountIdentifier).ifPresent(account -> account.setEnabled(true));
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManager#disable(org.salespointframework.useraccount.UserAccountIdentifier)
	 */
	@Override
	@Transactional
	public void disable(UserAccountIdentifier userAccountIdentifier) {

		Assert.notNull(userAccountIdentifier, "UserAccountIdentifier must not be null!");

		get(userAccountIdentifier).ifPresent(account -> account.setEnabled(false));
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManager#changePassword(org.salespointframework.useraccount.UserAccount, java.lang.String)
	 */
	@Override
	@Transactional
	public void changePassword(UserAccount userAccount, UnencryptedPassword password) {

		Assert.notNull(userAccount, "userAccount must not be null");
		Assert.notNull(password, "password must not be null");

		userAccount.setPassword(encrypt(password.asString()));
		save(userAccount);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManager#contains(org.salespointframework.useraccount.UserAccountIdentifier)
	 */
	@Override
	public boolean contains(UserAccountIdentifier userAccountIdentifier) {

		Assert.notNull(userAccountIdentifier, "userAccountIdentifier must not be null");
		return repository.existsById(userAccountIdentifier);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManager#findAll()
	 */
	@Override
	public Streamable<UserAccount> findAll() {
		return Streamable.of(repository.findAll());
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManager#findEnabled()
	 */
	@Override
	public Streamable<UserAccount> findEnabled() {
		return repository.findByEnabledTrue();
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManager#findDisabled()
	 */
	@Override
	public Streamable<UserAccount> findDisabled() {
		return repository.findByEnabledFalse();
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManager#findByUsername(java.lang.String)
	 */
	@Override
	public Optional<UserAccount> findByUsername(String username) {

		Assert.hasText(username, "Username must not be null or empty!");
		return repository.findById(new UserAccountIdentifier(username));
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManager#delete(org.salespointframework.useraccount.UserAccount)
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
	 * @param password must not be {@literal null} or empty.
	 * @return
	 */
	private EncryptedPassword encrypt(String password) {

		Assert.hasText(password, "Password must not be null or empty!");

		return EncryptedPassword.of(passwordEncoder.encode(password));
	}
}
