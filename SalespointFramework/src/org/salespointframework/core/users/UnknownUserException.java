package org.salespointframework.core.users;

public class UnknownUserException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public UnknownUserException(String identifier)
	{
		super("User " + identifier + " is unknown");
	}
}
