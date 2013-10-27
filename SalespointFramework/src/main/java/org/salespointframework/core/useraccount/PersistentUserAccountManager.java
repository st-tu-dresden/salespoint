package org.salespointframework.core.useraccount;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.salespointframework.util.Iterables;
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
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		
		CriteriaQuery<UserAccount> cq = cb.createQuery(UserAccount.class);
		Root<UserAccount> from = cq.from(UserAccount.class);
		cq.select(from);
		
		TypedQuery<UserAccount> tq = entityManager.createQuery(cq);
		
		return Iterables.of(tq.getResultList());
	}

}
