package org.salespointframework.core.user;

/**
 * If a User already exists with this UserId this Exception should be thrown.
 * 
 * @author Christopher Bellmann
 * 
 */

// TODO nötig?
@SuppressWarnings("serial")
public class DuplicateUserException extends IllegalArgumentException
{
	public DuplicateUserException(UserIdentifier userIdentifier)
	{
		super("User " + userIdentifier + " already exists");
	}
}
