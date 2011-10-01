package org.salespointframework.core.user;

import javax.persistence.Embeddable;

import org.salespointframework.util.SalespointIdentifier;

/**
 * Entity implementation class for Entity: UserIdentifier (TODO?)
 * 
 * @author Hannes Weisbach
 */
@SuppressWarnings("serial")
@Embeddable
public final class UserIdentifier extends SalespointIdentifier
{
	/**
	 * Creates a new unique identifier for {@link User}s.
	 */
	public UserIdentifier()
	{
		super();
	}

	/**
	 * TODO
	 * @param userIdentifier
	 */
	public UserIdentifier(String userIdentifier)
	{
		super(userIdentifier);
	}
}
