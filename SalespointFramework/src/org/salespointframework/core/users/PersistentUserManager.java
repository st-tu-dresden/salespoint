package org.salespointframework.core.users;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.salespointframework.core.database.Database;
import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;

public class PersistentUserManager implements UserManager<PersistentUser> {

	private final EntityManager entityManager;

	/**
	 * creates new AbstractUserManager
	 * 
	 * @param entityManager
	 */
	public PersistentUserManager(final EntityManager entityManager) {
		this.entityManager = Objects.requireNonNull(entityManager,
				"entityManager");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.salespointframework.core.users.UserManage#addUser(T)
	 */
	@Override
	public boolean addUser(final PersistentUser user) {
		Objects.requireNonNull(user, "user");
		if (entityManager.find(PersistentUser.class, user.getUserIdentifier()) != null) {
			return false;
		}
		entityManager.persist(user);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.salespointframework.core.users.UserManage#removeUser(T)
	 */
	@Override
	public boolean removeUser(final PersistentUser user) {
		Objects.requireNonNull(user, "user");
		Object token = null;
		// If user is logged on, log him off.
		if (userTokenMap.containsValue(user)) {
			for (Entry<Object, PersistentUser> e : userTokenMap.entrySet()) {
				if (e.getValue().equals(user)) {
					token = e.getKey();
					break;
				}
			}
			logOff(token);
		}
		// TODO Now delete him!

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.salespointframework.core.users.UserManage#addCapability(T,
	 * org.salespointframework.core.users.UserCapability)
	 */
	@Override
	public boolean addCapability(PersistentUser user,
			UserCapability userCapability) {
		Objects.requireNonNull(user, "user");
		Objects.requireNonNull(userCapability, "userCapability");

		return user.addCapability(userCapability);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.salespointframework.core.users.UserManage#removeCapability(T,
	 * org.salespointframework.core.users.UserCapability)
	 */
	@Override
	public boolean removeCapability(PersistentUser user,
			UserCapability userCapability) {
		Objects.requireNonNull(user, "user");
		Objects.requireNonNull(userCapability, "userCapability");

		return user.removeCapability(userCapability);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.salespointframework.core.users.UserManage#hasCapability(T,
	 * org.salespointframework.core.users.UserCapability)
	 */
	@Override
	public boolean hasCapability(PersistentUser user,
			UserCapability userCapability) {
		Objects.requireNonNull(user, "user");
		Objects.requireNonNull(userCapability, "userCapability");

		return user.hasCapability(userCapability);
	}

	public Iterable<UserCapability> getCapabilities(PersistentUser user) {
		Objects.requireNonNull(user, "user");
		return Iterables.from(user.getCapabilities());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.salespointframework.core.users.UserManage#getUsers()
	 */
	@Override
	public <T extends PersistentUser> Iterable<T> getUsers(Class<T> clazz) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(clazz);
		// create and execute a query, where all results are from type clazz
		TypedQuery<T> tq = entityManager.createQuery(q.where(q.from(clazz)
				.type().in(clazz)));

		return Iterables.from(tq.getResultList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.salespointframework.core.users.UserManage#getUserByIdentifier(org
	 * .salespointframework.core.users.UserIdentifier)
	 */
	@Override
	public <T extends PersistentUser> T getUserByIdentifier(Class<T> clazz,
			UserIdentifier userIdentifier) {
		Objects.requireNonNull(userIdentifier, "userIdentifier");
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(clazz);
		Root<T> r = q.from(clazz);
		Predicate primaryKey = cb.equal(r.get(PersistentUser_.userIdentifier),
				userIdentifier);
		Predicate type = r.type().in(clazz);
		q.where(cb.and(primaryKey, type));
		TypedQuery<T> tq = entityManager.createQuery(q);

		return tq.getSingleResult();
	}

	// PAAAAAAAAAAAAAAAAAAAAAAAAAAUUUUUUUUUUUUUUL
	private final Map<Object, PersistentUser> userTokenMap = new ConcurrentHashMap<Object, PersistentUser>();

	// TODO naming kinda sucks

	// associates a user with a token
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.salespointframework.core.users.UserManage#logOn(T,
	 * java.lang.Object)
	 */
	// TODO really? no return type, but throwing exceptions?
	@Override
	public final void logOn(PersistentUser user, Object token) {
		Objects.requireNonNull(user, "user");
		Objects.requireNonNull(token, "token");

		PersistentUser temp = this.getUserByIdentifier(PersistentUser.class,
				user.getUserIdentifier());

		if (temp == null) {
			throw new UnknownUserException(user.getUserIdentifier().toString());
		}

		if (user != null) {
			userTokenMap.put(token, user);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.salespointframework.core.users.UserManage#logOff(java.lang.Object)
	 */
	@Override
	public final void logOff(final Object token) {
		Objects.requireNonNull(token, "token");
		userTokenMap.remove(token);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.salespointframework.core.users.UserManage#getUserByToken(java.lang
	 * .Object)
	 */
	
	@Override
	public final <T extends PersistentUser> T getUserByToken(Class<T> clazz,
			Object token) {
		Objects.requireNonNull(token, "token");
		PersistentUser user = userTokenMap.get(token);
		if (clazz.isInstance(user)) {
			return clazz.cast(user);
		} else {
			throw new ClassCastException();
		}
	}

	// TODO Wozu die beiden Methoden sind erklär ich später, sollten aber
	// bleiben.
	// TODO ich weiß wozu die da sind, aber sollten wir das nicht einfach um
	// jedes persist/update/remove machen? Ist so eine Methode wirklich besser?
	private final void beginCommit(final EntityManager em) {
		if (entityManager == null) {
			em.getTransaction().begin();
			em.getTransaction().commit();
		}
	}

	// ?? Operator in C#, gibts hier leider nicht, deswegen die Methode
	private final EntityManager foobar() {
		return entityManager != null ? entityManager : Database.INSTANCE
				.getEntityManagerFactory().createEntityManager();
	}
}
