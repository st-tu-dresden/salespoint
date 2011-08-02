package org.salespointframework.core.users;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;

import org.salespointframework.core.database.ICanHasClass;
import org.salespointframework.util.Objects;
import org.salespointframework.util.SalespointIterable;


public abstract class AbstractUserManager<T extends User> implements ICanHasClass<T>{
	
		
	@PersistenceUnit
	private EntityManager entityManager;
	
	@ElementCollection
    protected Map<UserIdentifier, UserCapabilityList> capabilities = new HashMap<UserIdentifier, UserCapabilityList>();
	
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
		return true;
		
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
		if (this.getUserById(user.getUserId()) == null){
			//System.out.println("noSuchUser");
			return false;
		}
		else{
		     UserCapabilityList capList = capabilities.get(user);

		        if (capList  == null) {
		            //System.out.println("noCapas");
		        	capList = new UserCapabilityList();
		            capabilities.put(user.getUserId(), capList);
		        }
		        else {
		            if (capList.contains(userCapability)) return true;		          
		        }
		        System.out.println("AddCapa");
		        capList.add(userCapability);
		        return true;
		}
	}
	
	/**
	 * removes a UserCapability from an User. 
	 * @param user the User you want to give that UserCapability
	 * @param userCapability the Capapbility you want to give to the User
	 * @return true if successful, false if there is no such user
	 */
	public boolean removeCapability(T user, UserCapability userCapability){
		Objects.requireNonNull(user, "removeCapability_User");
		Objects.requireNonNull(userCapability, "removeCapability_userCapability");
		if (this.getUserById(user.getUserId()) == null){
			System.out.println("noSuchUser");
			return false;
		}
		else{
		    UserCapabilityList capList = capabilities.get(user);

		        if (capList  == null) {
		            //System.out.println("noCapas");
		        	return true;
		        }
		        else {
		            if (capList.contains(userCapability)){
		            	capList.remove(userCapability);
		            	 return true;
		            }
		            return false;
		        }
		        
		}
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
		if(capabilities.containsKey(user.getUserId())){
			UserCapabilityList ucl= capabilities.get(user.getUserId());
			if (ucl.contains(userCapability)) return true;
		}
		return false;
	}
	
	public Iterable<T> getUsers(){
		Class<T> cc = this.getContentClass();
		TypedQuery<T> users = entityManager.createQuery("SELECT t FROM " + cc.getSimpleName() + " t",cc);
		return SalespointIterable.from(users.getResultList());
	}
	
	
	public T getUserById(UserIdentifier userIdentifier){
		return entityManager.find(this.getContentClass(), userIdentifier);
	}

	
	
	
}
