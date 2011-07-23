package org.salespointframework.core.users;

/**
 * If a User already exists with this UserId this Exception should be thrown.
 * 
 * @author Christopher Bellmann
 *
 */
public class DuplicateUserException extends IllegalArgumentException{

	private static final long serialVersionUID = -2839717493990722789L;
	
	public DuplicateUserException(String userId){
		super("User "+ userId+ " already exists!");
	}


}
