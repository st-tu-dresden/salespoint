package org.salespointframework.core.users;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnit;

import org.salespointframework.util.Objects;
import org.salespointframework.util.SalespointIterable;


public class AbstractUserManager<T extends User>{
	
	//noch gut was zu tun!
	
	@PersistenceUnit
	private EntityManager entityManager;
	
	public AbstractUserManager(){
		
	}

	
	/**
	 * adds an User to the Usermanager if not exists and persists it
	 * @param user User you want to add
	 */
	public boolean addUser(User user){
		Objects.requireNonNull(user, "addUser");
		if (entityManager.contains(user)){
			return false;
		}
		else{
			entityManager.persist(user);
			return true;
		}
	}
	
	/**
	 * adds a UserCapability to an User
	 * @param user the User you want to give that UserCapability
	 * @param userCapability the Capapbility you want to give to the User
	 * @return true if successful 
	 */
	public boolean addCapability(User user, UserCapability userCapability){
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
	public boolean removeCapability(User user, UserCapability userCapability){
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
	public boolean hasCapability(User user,UserCapability userCapability){
		boolean i=true; //nur zür überbrückung!
		if(i){//TODO: check
			return true;
		}
		return false;
	}
	
	public Iterable<User> getAllUsers(){
		Collection<User> users= entityManager.createQuery("").getResultList();
		
		return SalespointIterable.from(users);
	}
	
	
	public User getUserByUsername(String username){
		return entityManager.find(User.class, username);
	}




	
	
}
