package org.salespointframework.core.useraccount;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class PersistentUserAccountManager implements UserAccountManager {

	@PersistenceContext
	private EntityManager entityManager;

	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Override
	public UserAccount create(UserAccountIdentifier userAccountIdentifier,
			String password, Role... roles) {
		UserAccount userAccount = new UserAccount(userAccountIdentifier,
				password, roles);
		return userAccount;
	}

	@Override
	public UserAccount get(UserAccountIdentifier userAccountIdentifier) {
		return entityManager.find(UserAccount.class, userAccountIdentifier);
	}

	@Override
	public void save(final UserAccount userAccount) {
		UserAccount userAccount2 = this.get(userAccount.getIdentifier());
		if (userAccount2 == null) {
			if (!userAccount.getPassword().isEncrypted()) {
				Password password = new Password(
						passwordEncoder.encode(userAccount.getPassword()
								.toString()), true);
				userAccount.setPassword(password);
			}
			entityManager.persist(userAccount);
		} else {
			// TODO Merge??
		}

	}

	@Override
	public void enable(UserAccountIdentifier userAccountIdentifier) {
		UserAccount userAccount = this.get(userAccountIdentifier);
		if (userAccount != null) {
			userAccount.setEnabled(true);
		}

	}

	@Override
	public void disable(UserAccountIdentifier userAccountIdentifier) {
		UserAccount userAccount = this.get(userAccountIdentifier);
		if (userAccount != null) {
			userAccount.setEnabled(false);
		}

	}

	@Override
	public void changePassword(UserAccount userAccount, String password) {
		userAccount = this.get(userAccount.getIdentifier());
		userAccount.setPassword(new Password(password));
	}

	@Override
	public boolean contains(UserAccountIdentifier userAccountIdentifier) {
		return entityManager.find(UserAccount.class, userAccountIdentifier) != null;
	}

	@Override
	public Iterable<UserAccount> findAll() {
		return null;
	}

}
