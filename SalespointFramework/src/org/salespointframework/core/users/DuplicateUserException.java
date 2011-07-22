package org.salespointframework.core.users;

/**
 * If a User already exists with this UserId this Exception should be thrown.
 * 
 * @author Christopher Bellmann
 *
 */
public class DuplicateUserException extends Exception{

	private static final long serialVersionUID = -2839717493990722789L;
	
	public DuplicateUserException(){
		super("User already exists!");
	}


}
