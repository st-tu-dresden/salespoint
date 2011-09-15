package org.salespointframework.core.users;

/**
 * If a User already exists with this UserId this Exception should be thrown.
 * 
 * @author Christopher Bellmann
 * 
 */
@SuppressWarnings("serial")
public class DuplicateUserException extends IllegalArgumentException
{
	public DuplicateUserException(UserIdentifier userIdentifier)
	{
		super("User " + userIdentifier + " already exists");
	}
}
