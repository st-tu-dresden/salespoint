package org.salespointframework.core.users;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.salespointframework.core.database.Database;
import org.salespointframework.core.database.ICanHasClass;
import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;


public abstract class AbstractUserManager<T extends AbstractUser> implements UserManager<T>, ICanHasClass<T> {
	
	private final EntityManager entityManager;
		
	/**
	 * creates new AbstractUserManager
	 * @param entityManager
	 */
	public AbstractUserManager(EntityManager entityManager){
		Objects.requireNonNull(entityManager, "entityManager");
		this.entityManager = entityManager;
	}

	
	/* (non-Javadoc)
	 * @see org.salespointframework.core.users.UserManage#addUser(T)
	 */
	@Override
	public boolean addUser(T user) {
		Objects.requireNonNull(user, "user");
		if (entityManager.find(this.getContentClass(), user.getUserIdentifier()) != null){
			throw new DuplicateUserException(user.getUserIdentifier());			
		}
		entityManager.persist(user);
//		UserCapabilityList capList = new UserCapabilityList(user.getUserId());
//        entityManager.persist(capList);
		return true;
		
	}
	
	/* (non-Javadoc)
	 * @see org.salespointframework.core.users.UserManage#removeUser(T)
	 */
	@Override
	public boolean removeUser(T user){
		Objects.requireNonNull(user, "user");
		return false;
	}
	
	
	/* (non-Javadoc)
	 * @see org.salespointframework.core.users.UserManage#addCapability(T, org.salespointframework.core.users.UserCapability)
	 */
	@Override
	public boolean addCapability(T user, UserCapability userCapability){
		Objects.requireNonNull(user, "user");
		Objects.requireNonNull(userCapability, "userCapability");
		
		user.addCapability(userCapability);
		
		return true;
		/*
		if (entityManager.find(this.getContentClass(), user.getUserIdentifier()) == null){
			//System.out.println("no such User");
			return false;
		}
		else{
		    
			UserCapabilityList capList = entityManager.find(UserCapabilityList.class, user.getUserIdentifier());
			System.out.println(capList);	
			
	        if (capList == null) {
	            //System.out.println("no Capabilities");
	        	capList = new UserCapabilityList(user.getUserIdentifier());
	            entityManager.persist(capList);
	            System.out.println("persList");
	        }
	        if (capList.contains(userCapability)) return true; //allready has Capabiltiy
	        else{
	        	capList.addCapa(userCapability);
	        	System.out.println("addCapa");
	        	System.out.println(capList);
	        	return true;
	        }
		}
		*/
	}
	
	/* (non-Javadoc)
	 * @see org.salespointframework.core.users.UserManage#removeCapability(T, org.salespointframework.core.users.UserCapability)
	 */
	@Override
	public boolean removeCapability(T user, UserCapability userCapability){
		Objects.requireNonNull(user, "user");
		Objects.requireNonNull(userCapability, "userCapability");

		user.removeCapability(userCapability);
		return true;
		
		/*
		UserCapabilityList ucl= entityManager.find(UserCapabilityList.class, user.getUserIdentifier());
		if(ucl==null) return false; //nosuchUser
		if(ucl.contains(userCapability)){
			ucl.remove(userCapability);
			return true;
		}
		return false; //userhasNotCapa
		*/
	}
	
	
	/* (non-Javadoc)
	 * @see org.salespointframework.core.users.UserManage#hasCapability(T, org.salespointframework.core.users.UserCapability)
	 */
	@Override
	public boolean hasCapability(T user, UserCapability userCapability){
		Objects.requireNonNull(user, "user");
		Objects.requireNonNull(userCapability, "userCapability");
		
		// TODO das geht auch schöner
		return Iterables.toSet(user.getCapabilities()).contains(userCapability);
		
		/*
		UserCapabilityList ucl= entityManager.find(UserCapabilityList.class, user.getUserIdentifier());
		if(ucl==null) return false;
		if(ucl.contains(userCapability)) return true;
		return false;
		*/
	}
	
	public Iterable<UserCapability> getCapabilities(T user) {
		Objects.requireNonNull(user, "user");
		return Iterables.from(user.getCapabilities());
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.salespointframework.core.users.UserManage#getUsers()
	 */
	@Override
	public Iterable<T> getUsers(){
		Class<T> cc = this.getContentClass();
		TypedQuery<T> users = entityManager.createQuery("SELECT t FROM " + cc.getSimpleName() + " t",cc);
		return Iterables.from(users.getResultList());
	}
	
	
	/* (non-Javadoc)
	 * @see org.salespointframework.core.users.UserManage#getUserByIdentifier(org.salespointframework.core.users.UserIdentifier)
	 */
	@Override
	public T getUserByIdentifier(UserIdentifier userIdentifier){
		Objects.requireNonNull(userIdentifier, "userIdentifier");
		return entityManager.find(this.getContentClass(), userIdentifier);
	}
	
	
	// PAAAAAAAAAAAAAAAAAAAAAAAAAAUUUUUUUUUUUUUUL
	private final Map<Object, T> userTokenMap = new ConcurrentHashMap<Object, T>();

	// TODO naming kinda sucks
	
	// associates a user with a token
    /* (non-Javadoc)
	 * @see org.salespointframework.core.users.UserManage#logOn(T, java.lang.Object)
	 */
    @Override
	public final void logOn(T user, Object token) {
    	Objects.requireNonNull(user, "user");
    	Objects.requireNonNull(token, "token");
    	
        T temp = this.getUserByIdentifier(user.getUserIdentifier());

        if (temp == null) {
            throw new UnknownUserException(user.getUserIdentifier().toString());
        }

        if (user != null) {
            userTokenMap.put(token, user);
        }
    }

    /* (non-Javadoc)
	 * @see org.salespointframework.core.users.UserManage#logOff(java.lang.Object)
	 */
    @Override
	public final T logOff(Object token) {
    	Objects.requireNonNull(token, "token");
    	T user = userTokenMap.remove(token);
        return user;
    }

    /* (non-Javadoc)
	 * @see org.salespointframework.core.users.UserManage#getUserByToken(java.lang.Object)
	 */
    @Override
	public final T getUserByToken(Object token) {
    	Objects.requireNonNull(token, "token");
        return userTokenMap.get(token);
    }
    
    // Wozu die beiden Methoden sind erklär ich später, sollten aber bleiben.
	private final void beginCommit(final EntityManager em) {
		if(entityManager == null) {
			em.getTransaction().begin();
			em.getTransaction().commit();
		}
	}
	
	// ?? Operator in C#, gibts hier leider nicht, deswegen die Methode
	private final EntityManager foobar() {
		return entityManager != null ? entityManager : Database.INSTANCE.getEntityManagerFactory().createEntityManager();
	}
	
	
	
}
