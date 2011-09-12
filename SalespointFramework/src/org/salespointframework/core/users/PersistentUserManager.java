package org.salespointframework.core.users;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.salespointframework.core.database.Database;
import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;

/**
 * @author Christopher Bellmann
 * @author Paul Henke
 * @author Hannes Weissbach
 */
// FIXME
public class PersistentUserManager implements UserManager<PersistentUser> {

	private final EntityManagerFactory emf = Database.INSTANCE.getEntityManagerFactory();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.salespointframework.core.users.UserManage#addUser(T)
	 */
	@Override
	public boolean addUser(final PersistentUser user) {
		Objects.requireNonNull(user, "user");
		EntityManager em = emf.createEntityManager();
		if (em.find(PersistentUser.class, user.getUserIdentifier()) != null) {
			return false;
		}
		em.persist(user);
		beginCommit(em);
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
		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(clazz);
		// create and execute a query, where all results are from type clazz
		TypedQuery<T> tq = em.createQuery(q.where(q.from(clazz)
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
	public <T extends PersistentUser> T get(Class<T> clazz,
			UserIdentifier userIdentifier) {
		Objects.requireNonNull(userIdentifier, "userIdentifier");
		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(clazz);
		Root<T> r = q.from(clazz);
		Predicate primaryKey = cb.equal(r.get(PersistentUser_.userIdentifier),
				userIdentifier);
		Predicate type = r.type().in(clazz);
		q.where(cb.and(primaryKey, type));
		TypedQuery<T> tq = em.createQuery(q);

		return tq.getSingleResult();
	}
	
	public void update(PersistentUser user) {
		EntityManager em = emf.createEntityManager();
		em.merge(user);
		beginCommit(em);
	}

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

		PersistentUser temp = this.get(PersistentUser.class,
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
	
	private void beginCommit(EntityManager entityManager) {
		entityManager.getTransaction().begin();
		entityManager.getTransaction().commit();
	}
}
