package org.salespointframework.core.user;

import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.servlet.http.HttpSession;

import org.salespointframework.core.database.Database;
import org.salespointframework.util.Iterables;
import org.salespointframework.web.WebAuthenticationManager;

/**
 * The <code>PersistentUserManager</code> is an implementation of
 * <code>UserManager</code> that aggregates <code>PersistentUser</code>s and
 * stores them in the underlying database.
 * 
 * @author Christopher Bellmann
 * @author Paul Henke
 * @author Hannes Weissbach
 */
public class PersistentUserManager implements UserManager<PersistentUser>
{
	private final EntityManagerFactory emf = Database.INSTANCE.getEntityManagerFactory();

	@Override
	public void add(PersistentUser user)
	{
		Objects.requireNonNull(user, "user must not be null");
		EntityManager em = emf.createEntityManager();
		em.persist(user);
		beginCommit(em);
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

		EntityManager em = emf.createEntityManager();
		Object user = em.find(PersistentUser.class, userIdentifier);
		if (user != null) {
			em.remove(user);
			beginCommit(em);
			return true;
		}
		
		return false;
	}

	@Override
	public boolean contains(UserIdentifier userIdentifier)
	{
		Objects.requireNonNull(userIdentifier, "userIdentifier must not be null");

		EntityManager em = emf.createEntityManager();
		return em.find(PersistentUser.class, userIdentifier) != null;
	}

	@Override
	public <T extends PersistentUser> Iterable<T> find(Class<T> clazz)
	{
		Objects.requireNonNull(clazz, "clazz must not be null");

		EntityManager em = emf.createEntityManager();
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

		EntityManager em = emf.createEntityManager();
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

		EntityManager em = emf.createEntityManager();
		em.merge(user);
		beginCommit(em);
	}

	
	// TODO use WeakHashMap http://docs.oracle.com/javase/6/docs/api/java/util/WeakHashMap.html
	// verhindert dass tote Session für immer und ewig in der Map bleiben, normalerweise egal, außer bei "real world" anwendungen die nicht nur 30min laufen 
	//private static final Map<Object, User> userTokenMap = new ConcurrentHashMap<>();

	
	// TODO Methode ganz entfernen
	@Override
	@Deprecated
	public final void login(PersistentUser user, Object token)
	{
		Objects.requireNonNull(user, "user must not be null");
		Objects.requireNonNull(token, "token must not be null");

		
		WebAuthenticationManager.INSTANCE.login(user, (HttpSession) token);
		
		// TODO cache bug >_<
		//PersistentUser temp = this.get(PersistentUser.class, user.getIdentifier());

		//userTokenMap.put(token, user);
	}

	// TODO Methode ganz entfernen
	@Override
	@Deprecated
	public final void logout(Object token)
	{
		Objects.requireNonNull(token, "token must not be null");
		
		WebAuthenticationManager.INSTANCE.logout((HttpSession) token);

		//userTokenMap.remove(token);
	}

	// TODO Methode ganz entfernen
	@Override
	@Deprecated
	public final <T extends PersistentUser> T getUserByToken(Class<T> clazz, Object token)
	{
		Objects.requireNonNull(clazz, "clazz must not be null");
		Objects.requireNonNull(token, "token must not be null");

		
		//User user = userTokenMap.get(token);
		User user = WebAuthenticationManager.INSTANCE.getUser((HttpSession)token);				
		
		if(user == null)
		{
			return null;
		}
		
		
		// FIXME user cache bug
		//EntityManager em = emf.createEntityManager();
		
		//return em.find(clazz, user.getIdentifier());
		
		
		if (clazz.isInstance(user))
			return clazz.cast(user);
		
		throw new ClassCastException();
		
		
	}

	private void beginCommit(EntityManager entityManager)
	{
		entityManager.getTransaction().begin();
		entityManager.getTransaction().commit();
	}
}
