package org.salespointframework.core.users;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;

import org.salespointframework.core.database.ICanHasClass;
import org.salespointframework.util.Objects;
import org.salespointframework.util.SalespointIterable;


public abstract class AbstractUserManager<T extends User> implements ICanHasClass<T>{
	
		
	@PersistenceUnit
	private EntityManager entityManager;
	
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
	 * @return true if successful 
	 */
	public boolean addCapability(T user, UserCapability userCapability){
		Objects.requireNonNull(user, "addCapability_User");
		Objects.requireNonNull(userCapability, "addCapability_userCapability");
		if (!entityManager.contains(user)){
			return false;
		}
		else{
			if(!entityManager.contains(userCapability)){
				entityManager.persist(userCapability);
			}
			//TODO: add Cap to User
			return true;
		}
	}
	
	/**
	 * removes a UserCapability from an User. 
	 * @param user the User you want to give that UserCapability
	 * @param userCapability the Capapbility you want to give to the User
	 * @return true if successful 
	 */
	public boolean removeCapability(T user, UserCapability userCapability){
		Objects.requireNonNull(user, "removeCapability_User");
		Objects.requireNonNull(userCapability, "removeCapability_userCapability");
		if (!entityManager.contains(user)){
			return false;
		}
		else{
			boolean i=true; //nur zür überbrückung!
			if(i){//check if User has Capability exists
				//TODO: remove Capability
				return true;
			}
			else return false;
		}
	}
	
	/**
	 * Checks if a User has the given Capability
	 * @param user you want to check
	 * @param userCapability the cabability you want to check to User for
	 * @return true if User has Capability
	 */
	public boolean hasCapability(T user,UserCapability userCapability){
		boolean i=true; //nur zür überbrückung!
		if(i){//TODO: check
			return true;
		}
		return false;
	}
	
	public Iterable<T> getUsers(){
		Class<T> cc = this.getContentClass();
		TypedQuery<T> users = entityManager.createQuery("Select t from " + cc.getCanonicalName() + " t",cc);
		return SalespointIterable.from(users.getResultList());
	}
	
	
	public T getUserById(String userId){
		return entityManager.find(this.getContentClass(), userId);
	}

	
	
	
}
