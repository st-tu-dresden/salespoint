package org.salespointframework.core.user;

// TODO nötig?

@SuppressWarnings("serial")
public class UnknownUserException extends RuntimeException
{
	public UnknownUserException(UserIdentifier userIdentifier)
	{
		super("User " + userIdentifier.toString() + " is unknown");
	}
}
