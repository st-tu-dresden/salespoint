package org.salespointframework.core.user;

public class UnknownUserException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public UnknownUserException(String identifier)
	{
		super("User " + identifier + " is unknown");
	}
}
