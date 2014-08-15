package org.salespointframework.useraccount;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

		Objects.requireNonNull(repository, "UserAccountRepository must not be null!");
		Objects.requireNonNull(passwordEncoder, "PasswordEncoder must not be null!");

		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManager#create(java.lang.String, java.lang.String, org.salespointframework.useraccount.Role[])
	 */
	@Override
	public UserAccount create(String userName, String password, Role... roles) {

		Objects.requireNonNull(userName, "userName must not be null");
		Objects.requireNonNull(password, "password must not be null");
		Objects.requireNonNull(roles, "roles must not be null");

		UserAccountIdentifier identifier = new UserAccountIdentifier(userName);
		
		return new UserAccount(identifier, password, roles);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManager#get(org.salespointframework.useraccount.UserAccountIdentifier)
	 */
	@Override
	public UserAccount get(UserAccountIdentifier userAccountIdentifier) {

		Objects.requireNonNull(userAccountIdentifier, "userAccountIdentifier must not be null");
		return repository.findOne(userAccountIdentifier);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManager#save(org.salespointframework.useraccount.UserAccount)
	 */
	@Override
	@Transactional
	public UserAccount save(UserAccount userAccount) {

		Objects.requireNonNull(userAccount, "userAccount must not be null");

		Password password = userAccount.getPassword();
		Password passwordToSet = password.isEncrypted() ? password : new Password(passwordEncoder.encode(password
				.toString()), true);
		userAccount.setPassword(passwordToSet);
		return repository.save(userAccount);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManager#enable(org.salespointframework.useraccount.UserAccountIdentifier)
	 */
	@Override
	public void enable(UserAccountIdentifier userAccountIdentifier) {
		Objects.requireNonNull(userAccountIdentifier, "userAccountIdentifier must not be null");
		UserAccount userAccount = this.get(userAccountIdentifier);
		if (userAccount != null) {
			userAccount.setEnabled(true);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManager#disable(org.salespointframework.useraccount.UserAccountIdentifier)
	 */
	@Override
	public void disable(UserAccountIdentifier userAccountIdentifier) {
		
		Objects.requireNonNull(userAccountIdentifier, "userAccountIdentifier must not be null");
		UserAccount userAccount = this.get(userAccountIdentifier);
		
		if (userAccount != null) {
			userAccount.setEnabled(false);
		}

		save(userAccount);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManager#changePassword(org.salespointframework.useraccount.UserAccount, java.lang.String)
	 */
	@Override
	@Transactional
	public void changePassword(UserAccount userAccount, String password) {

		Objects.requireNonNull(userAccount, "userAccount must not be null");
		Objects.requireNonNull(password, "password must not be null");

		userAccount.setPassword(new Password(password));
		save(userAccount);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.useraccount.UserAccountManager#contains(org.salespointframework.useraccount.UserAccountIdentifier)
	 */
	@Override
	public boolean contains(UserAccountIdentifier userAccountIdentifier) {
		Objects.requireNonNull(userAccountIdentifier, "userAccountIdentifier must not be null");
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
}
