package org.salespointframework.useraccount;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
class PersistentUserAccountManager implements UserAccountManager {

	private final UserAccountRepository repository;
	private final PasswordEncoder passwordEncoder;

	/**
	 * Creates a new {@link PersistentUserAccountManager} using the given {@link UserAccountRepository} and
	 * {@link PasswordEncoder}.
	 * 
	 * @param repository must not be {@literal null}.
	 * @param passwordEncoder must not be {@literal null}.
	 */
	@Autowired
	public PersistentUserAccountManager(UserAccountRepository repository, PasswordEncoder passwordEncoder) {

		Assert.notNull(repository, "UserAccountRepository must not be null!");
		Assert.notNull(passwordEncoder, "PasswordEncoder must not be null!");

		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManager#create(java.lang.String, java.lang.String, org.salespointframework.useraccount.Role[])
	 */
	@Override
	public UserAccount create(String userName, String password, Role... roles) {

		Assert.notNull(userName, "userName must not be null");
		Assert.notNull(password, "password must not be null");
		Assert.notNull(roles, "roles must not be null");

		// Reject username if a user with that name already exists
		findByUsername(userName).ifPresent(user -> {
			throw new IllegalArgumentException(String.format("User with name %s already exists!", userName));
		});

		return new UserAccount(new UserAccountIdentifier(userName), password, roles);
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

		userAccount.setPassword(password.isEncrypted() ? password : new Password(
				passwordEncoder.encode(password.toString()), true));

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

		userAccount.setPassword(new Password(password));
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
	public Iterable<UserAccount> findAll() {
		return repository.findAll();
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManager#findEnabled()
	 */
	@Override
	public Iterable<UserAccount> findEnabled() {
		return repository.findByEnabledTrue();
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManager#findDisabled()
	 */
	@Override
	public Iterable<UserAccount> findDisabled() {
		return repository.findByEnabledFalse();
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
