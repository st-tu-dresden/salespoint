package org.salespointframework.useraccount;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.salespointframework.core.Streamable;
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

	private final @NonNull UserAccountRepository repository;
	private final @NonNull PasswordEncoder passwordEncoder;

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManager#create(java.lang.String, java.lang.String, org.salespointframework.useraccount.Role[])
	 */
	@Override
	@Transactional
	public UserAccount create(String userName, String password, Role... roles) {

		Assert.notNull(userName, "userName must not be null");
		Assert.notNull(password, "password must not be null");
		Assert.notNull(roles, "roles must not be null");

		// Reject username if a user with that name already exists
		findByUsername(userName).ifPresent(user -> {
			throw new IllegalArgumentException(String.format("User with name %s already exists!", userName));
		});

		return save(new UserAccount(new UserAccountIdentifier(userName), password, roles));
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManager#get(org.salespointframework.useraccount.UserAccountIdentifier)
	 */
	@Override
	public Optional<UserAccount> get(UserAccountIdentifier userAccountIdentifier) {

		Assert.notNull(userAccountIdentifier, "userAccountIdentifier must not be null");
		return repository.findOne(userAccountIdentifier);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManager#save(org.salespointframework.useraccount.UserAccount)
	 */
	@Override
	@Transactional
	public UserAccount save(UserAccount userAccount) {

		Assert.notNull(userAccount, "UserAccount must not be null!");

		Password password = userAccount.getPassword();

		userAccount.setPassword(
				password.isEncrypted() ? password : Password.encrypted(passwordEncoder.encode(password.getPassword())));

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
	public void changePassword(UserAccount userAccount, String password) {

		Assert.notNull(userAccount, "userAccount must not be null");
		Assert.notNull(password, "password must not be null");

		userAccount.setPassword(Password.unencrypted(password));
		save(userAccount);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManager#contains(org.salespointframework.useraccount.UserAccountIdentifier)
	 */
	@Override
	public boolean contains(UserAccountIdentifier userAccountIdentifier) {

		Assert.notNull(userAccountIdentifier, "userAccountIdentifier must not be null");
		return repository.exists(userAccountIdentifier);
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
		return Streamable.of(repository.findByEnabledTrue());
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManager#findDisabled()
	 */
	@Override
	public Streamable<UserAccount> findDisabled() {
		return Streamable.of(repository.findByEnabledFalse());
	}

	/* 
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManager#findByUsername(java.lang.String)
	 */
	@Override
	public Optional<UserAccount> findByUsername(String username) {

		Assert.hasText(username, "Username must not be null or empty!");
		return repository.findOne(new UserAccountIdentifier(username));
	}
}
