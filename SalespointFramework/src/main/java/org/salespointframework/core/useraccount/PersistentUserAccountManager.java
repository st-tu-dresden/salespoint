package org.salespointframework.core.useraccount;

import java.util.Objects;

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
	public UserAccount create(UserAccountIdentifier userAccountIdentifier, String password, Role... roles) {
		Objects.requireNonNull(userAccountIdentifier, "userAccountIdentifier must not be null");
		Objects.requireNonNull(password, "password must not be null");
		Objects.requireNonNull(roles, "roles must not be null");
		UserAccount userAccount = new UserAccount(userAccountIdentifier, password, roles);
		return userAccount;
	}

	@Override
	public UserAccount get(UserAccountIdentifier userAccountIdentifier) {
		Objects.requireNonNull(userAccountIdentifier, "userAccountIdentifier must not be null");
		return entityManager.find(UserAccount.class, userAccountIdentifier);
	}

	// TODO DRY
	@Override
	public void save(final UserAccount userAccount) {
		Objects.requireNonNull(userAccount, "userAccount must not be null");
		UserAccount userAccount2 = this.get(userAccount.getIdentifier());
		if (userAccount2 == null) {
			if (!userAccount.getPassword().isEncrypted()) {
				Password password = new Password(passwordEncoder.encode(userAccount.getPassword().toString()), true);
				userAccount.setPassword(password);
			}
			entityManager.persist(userAccount);
		} else {
			if (!userAccount2.getPassword().isEncrypted()) {
				Password password = new Password(passwordEncoder.encode(userAccount2.getPassword().toString()), true);
				userAccount2.setPassword(password);
			}
			entityManager.merge(userAccount2);
		}
	}

	@Override
	public void enable(UserAccountIdentifier userAccountIdentifier) {
		Objects.requireNonNull(userAccountIdentifier, "userAccountIdentifier must not be null");
		UserAccount userAccount = this.get(userAccountIdentifier);
		if (userAccount != null) {
			userAccount.setEnabled(true);
		}

	}

	@Override
	public void disable(UserAccountIdentifier userAccountIdentifier) {
		Objects.requireNonNull(userAccountIdentifier, "userAccountIdentifier must not be null");
		UserAccount userAccount = this.get(userAccountIdentifier);
		if (userAccount != null) {
			userAccount.setEnabled(false);
		}

	}

	@Override
	public void changePassword(UserAccount userAccount, String password) {
		Objects.requireNonNull(userAccount, "userAccount must not be null");
		Objects.requireNonNull(password, "password must not be null");
		userAccount = this.get(userAccount.getIdentifier());
		userAccount.setPassword(new Password(password));
	}

	@Override
	public boolean contains(UserAccountIdentifier userAccountIdentifier) {
		Objects.requireNonNull(userAccountIdentifier, "userAccountIdentifier must not be null");
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

	@Override
	public Iterable<UserAccount> findEnabled() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<UserAccount> cq = cb.createQuery(UserAccount.class);
		Root<UserAccount> entry = cq.from(UserAccount.class);
		cq.where(cb.isTrue(entry.get(UserAccount_.isEnabled)));
		TypedQuery<UserAccount> tq = entityManager.createQuery(cq);
		
		return Iterables.of(tq.getResultList());
	}

	@Override
	public Iterable<UserAccount> findDisabled() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<UserAccount> cq = cb.createQuery(UserAccount.class);
		Root<UserAccount> entry = cq.from(UserAccount.class);
		cq.where(cb.isFalse(entry.get(UserAccount_.isEnabled)));
		TypedQuery<UserAccount> tq = entityManager.createQuery(cq);
		
		return Iterables.of(tq.getResultList());
	}

	@Override
	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

}
