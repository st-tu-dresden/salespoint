package org.salespointframework.core.users;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.salespointframework.core.database.ICanHasClass;
import org.salespointframework.util.Objects;
import org.salespointframework.util.SalespointIterable;


public abstract class AbstractUserManager<T extends User> implements ICanHasClass<T>{
	
		
	private EntityManager entityManager;
	
		
	/**
	 * creates new AbstractUserManager
	 * @param entityManager
	 */
	public AbstractUserManager(EntityManager entityManager){
		this.entityManager= entityManager;
	}

	
	/**
	 * adds an User to the Usermanager if not exists and persists it
	 * @param user User you want to add
	 * @return 
	 * @throws DuplicateUserException 
	 */
	public boolean addUser(T user) {
		Objects.requireNonNull(user, "addUser");
		if (entityManager.find(user.getClass(), user.getUserId()) != null){
			throw new DuplicateUserException(user.getUserId());			
		}
		entityManager.persist(user);
//		UserCapabilityList capList = new UserCapabilityList(user.getUserId());
//        entityManager.persist(capList);
		return true;
		
	}
	
	/**
	 * will remove User, but only if there is no open Order for the User
	 * @param User user you want remove
	 * @return true if successful
	 */
	public boolean removeUser(T User){
		return false;
	}
	
	
	/**
	 * adds a UserCapability to an User
	 * @param user the User you want to give that UserCapability
	 * @param userCapability the Capapbility you want to give to the User
	 * @return true if successful, false if there is no such user
	 */
	public boolean addCapability(T user, UserCapability userCapability){
		Objects.requireNonNull(user, "addCapability_User");
		Objects.requireNonNull(userCapability, "addCapability_userCapability");
		if (entityManager.find(user.getClass(), user.getUserId()) == null){
			//System.out.println("no such User");
			return false;
		}
		else{
		    
			UserCapabilityList capList = entityManager.find(UserCapabilityList.class, user.getUserId());
			System.out.println(capList);	
			
	        if (capList == null) {
	            //System.out.println("no Capabilities");
	        	capList = new UserCapabilityList(user.getUserId());
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
	}
	
	/**
	 * removes a UserCapability from an User. 
	 * @param user the User you want to remove that UserCapability from
	 * @param userCapability the Capability you want to remove from the User
	 * @return true if successful, false if there is no such user or the user does not have this capability
	 */
	public boolean removeCapability(T user, UserCapability userCapability){
		Objects.requireNonNull(user, "removeCapability_User");
		Objects.requireNonNull(userCapability, "removeCapability_userCapability");

		UserCapabilityList ucl= entityManager.find(UserCapabilityList.class, user.getUserId());
		if(ucl==null) return false; //nosuchUser
		if(ucl.contains(userCapability)){
			ucl.remove(userCapability);
			return true;
		}
		return false; //userhasNotCapa

	}
	
	
	/**
	 * Checks if a User has the given Capability
	 * @param user you want to check
	 * @param userCapability the cabability you want to check to User for
	 * @return true if User has Capability
	 */
	public boolean hasCapability(T user, UserCapability userCapability){
		Objects.requireNonNull(user, "hasCapability_User");
		Objects.requireNonNull(userCapability, "hasCapability_userCapability");
		
		UserCapabilityList ucl= entityManager.find(UserCapabilityList.class, user.getUserId());
		if(ucl==null) return false;
		if(ucl.contains(userCapability)) return true;
		return false;
	}
	/**
	 * 
	 * @return all User from this Usermanger
	 */
	public Iterable<T> getUsers(){
		Class<T> cc = this.getContentClass();
		TypedQuery<T> users = entityManager.createQuery("SELECT t FROM " + cc.getSimpleName() + " t",cc);
		Iterable<T> i= SalespointIterable.from(users.getResultList());
		return i;
	}
	
	
	public T getUserById(UserIdentifier userIdentifier){
		return entityManager.find(this.getContentClass(), userIdentifier);
	}

	
	
	
}
