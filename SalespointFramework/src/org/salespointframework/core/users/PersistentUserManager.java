package org.salespointframework.core.users;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.salespointframework.core.database.Database;
import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;

/**
 * @author Christopher Bellmann
 * @author Paul Henke
 * @author Hannes Weissbach
 */

// FIXME
public class PersistentUserManager implements UserManager<PersistentUser>
{

	private final EntityManagerFactory emf = Database.INSTANCE.getEntityManagerFactory();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.salespointframework.core.users.UserManage#addUser(T)
	 */
	@Override
	public void add(final PersistentUser user)
	{
		Objects.requireNonNull(user, "user");

		EntityManager em = emf.createEntityManager();
		if (em.find(PersistentUser.class, user.getUserIdentifier()) != null)
		{
			throw new DuplicateUserException(user.getUserIdentifier());
		}
		em.persist(user);
		beginCommit(em);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.salespointframework.core.users.UserManage#removeUser(T)
	 */
	@Override
	public boolean remove(UserIdentifier userIdentifer)
	{
		Objects.requireNonNull(userIdentifer, "userIdentifer");

		// If user is logged on, log him off.
		for (Map.Entry<Object, PersistentUser> entry : userTokenMap.entrySet())
		{
			if (entry.getValue().getUserIdentifier().equals(userIdentifer))
			{
				Object token = entry.getKey();
				this.logOff(token);
				break;
			}
		}

		EntityManager em = emf.createEntityManager();
		Object user = em.find(PersistentUser.class, userIdentifer);
		if (user != null)
		{
			em.remove(user);
			beginCommit(em);
			return true;
		} else
		{
			return false;
		}
	}

	@Override
	public boolean contains(UserIdentifier userIdentifier)
	{
		Objects.requireNonNull(userIdentifier, "userIdentifier");

		EntityManager em = emf.createEntityManager();
		return em.find(PersistentUser.class, userIdentifier) != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.salespointframework.core.users.UserManage#addCapability(T,
	 * org.salespointframework.core.users.UserCapability)
	 */
	@Override
	public boolean addCapability(PersistentUser user, UserCapability userCapability)
	{
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
	public boolean removeCapability(PersistentUser user, UserCapability userCapability)
	{
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
	public boolean hasCapability(PersistentUser user, UserCapability userCapability)
	{
		Objects.requireNonNull(user, "user");
		Objects.requireNonNull(userCapability, "userCapability");

		return user.hasCapability(userCapability);
	}

	public Iterable<UserCapability> getCapabilities(PersistentUser user)
	{
		Objects.requireNonNull(user, "user");

		return Iterables.from(user.getCapabilities());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.salespointframework.core.users.UserManage#getUsers()
	 */
	@Override
	public <T extends PersistentUser> Iterable<T> find(Class<T> clazz)
	{
		Objects.requireNonNull(clazz, "clazz");

		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(clazz);
		TypedQuery<T> tq = em.createQuery(q.where(q.from(clazz).type().in(clazz)));

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
	public <T extends PersistentUser> T get(Class<T> clazz, UserIdentifier userIdentifier)
	{
		Objects.requireNonNull(clazz, "clazz");
		Objects.requireNonNull(userIdentifier, "userIdentifier");

		EntityManager em = emf.createEntityManager();
		return em.find(clazz, userIdentifier);

		/*
		 * why the hell must be things so complicated EntityManager em =
		 * emf.createEntityManager(); CriteriaBuilder cb =
		 * em.getCriteriaBuilder(); CriteriaQuery<T> cq = cb.createQuery(clazz);
		 * Root<T> entry = cq.from(clazz); Predicate primaryKey =
		 * cb.equal(entry.get(PersistentUser_.userIdentifier), userIdentifier);
		 * Predicate type = entry.type().in(clazz); cq.where(primaryKey, type);
		 * TypedQuery<T> tq = em.createQuery(cq);
		 * 
		 * return tq.getSingleResult();
		 */
	}

	public void update(PersistentUser user)
	{
		Objects.requireNonNull(user, "user");

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
	public final void logOn(PersistentUser user, Object token)
	{
		Objects.requireNonNull(user, "user");
		Objects.requireNonNull(token, "token");

		PersistentUser temp = this.get(PersistentUser.class, user.getUserIdentifier());

		if (temp == null)
		{
			throw new UnknownUserException(user.getUserIdentifier().toString());
		}

		if (user != null)
		{
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
	public final void logOff(final Object token)
	{
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
	public final <T extends PersistentUser> T getUserByToken(Class<T> clazz, Object token)
	{
		Objects.requireNonNull(clazz, "clazz");
		Objects.requireNonNull(token, "token");

		PersistentUser user = userTokenMap.get(token);
		if (clazz.isInstance(user))
		{
			return clazz.cast(user);
		} else
		{
			throw new ClassCastException();
		}
	}

	private void beginCommit(EntityManager entityManager)
	{
		entityManager.getTransaction().begin();
		entityManager.getTransaction().commit();
	}
}
