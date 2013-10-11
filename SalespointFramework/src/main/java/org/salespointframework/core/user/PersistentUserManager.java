package org.salespointframework.core.user;

import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.salespointframework.util.Iterables;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * The <code>PersistentUserManager</code> is an implementation of
 * <code>UserManager</code> that aggregates <code>PersistentUser</code>s and
 * stores them in the underlying database.
 * 
 * @author Christopher Bellmann
 * @author Paul Henke
 * @author Hannes Weissbach
 * @author Oliver Gierke
 */
@Component
@Transactional
class PersistentUserManager implements UserManager<PersistentUser>
{
	@PersistenceContext
	private  EntityManager em;

	@Override
	public void add(PersistentUser user)
	{
		Objects.requireNonNull(user, "user must not be null");
		em.persist(user);
	}

	@Override
	public boolean remove(UserIdentifier userIdentifier)
	{
		Objects.requireNonNull(userIdentifier, "userIdentifer must not be null");

		// TODO remove
		// If user is logged on, log him off.
		/*
		for (Map.Entry<Object, User> entry : userTokenMap.entrySet()) {
			if (entry.getValue().getIdentifier().equals(userIdentifier)) {
				Object token = entry.getKey();
				this.logout(token);
				break;
			}
		}
		*/

		Object user = em.find(PersistentUser.class, userIdentifier);
		if (user != null) {
			em.remove(user);
			return true;
		}
		
		return false;
	}

	@Override
	public boolean contains(UserIdentifier userIdentifier)
	{
		Objects.requireNonNull(userIdentifier, "userIdentifier must not be null");

		return em.find(PersistentUser.class, userIdentifier) != null;
	}

	@Override
	public <T extends PersistentUser> Iterable<T> find(Class<T> clazz)
	{
		Objects.requireNonNull(clazz, "clazz must not be null");

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		TypedQuery<T> tq = em.createQuery(cq);

		return Iterables.of(tq.getResultList());
	}

	@Override
	public <T extends PersistentUser> T get(Class<T> clazz, UserIdentifier userIdentifier)
	{
		Objects.requireNonNull(clazz, "clazz must not be null");
		Objects.requireNonNull(userIdentifier, "userIdentifier must not be null");

		return em.find(clazz, userIdentifier);
	}

    /**
     * Updates and persists an existing {@link PersistentUser} to the
     * {@link PersistentUserManager} and the Database
     * 
     * @param user
     *            the <code>PersistentUser</code> to be updated
     * @throws NullPointerException
     *             if <code>user</code> is <code>null</code>
     */
	public void update(PersistentUser user)
	{
		Objects.requireNonNull(user, "user must not be null");

		em.merge(user);
	}
}
