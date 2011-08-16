package org.salespointframework.core.users;

public interface UserManager<T extends User> {

	/**
	 * adds an User to the Usermanager if not exists and persists it
	 * @param user User you want to add
	 * @return 
	 * @throws DuplicateUserException 
	 */
	boolean addUser(T user);

	/**
	 * will remove User, but only if there is no open Order for the User
	 * @param User user you want remove
	 * @return true if successful
	 */
	boolean removeUser(T user);

	/**
	 * adds a UserCapability to an User
	 * @param user the User you want to give that UserCapability
	 * @param userCapability the Capapbility you want to give to the User
	 * @return true if successful, false if there is no such user
	 */
	boolean addCapability(T user, UserCapability userCapability);

	/**
	 * removes a UserCapability from an User. 
	 * @param user the User you want to remove that UserCapability from
	 * @param userCapability the Capability you want to remove from the User
	 * @return true if successful, false if there is no such user or the user does not have this capability
	 */
	boolean removeCapability(T user, UserCapability userCapability);

	/**
	 * Checks if a User has the given Capability
	 * @param user you want to check
	 * @param userCapability the cabability you want to check to User for
	 * @return true if User has Capability
	 */
	boolean hasCapability(T user, UserCapability userCapability);

	/**
	 * 
	 * @return all User from this Usermanger
	 */
	Iterable<T> getUsers();

	T getUserByIdentifier(UserIdentifier userIdentifier);

	// associates a user with a token
	void logOn(T user, Object token);

	T logOff(Object token);

	T getUserByToken(Object token);

}