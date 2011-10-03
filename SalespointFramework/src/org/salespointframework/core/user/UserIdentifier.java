package org.salespointframework.core.user;

import javax.persistence.Embeddable;

import org.salespointframework.util.SalespointIdentifier;

/**
 * <code>UserIdentifier</code> serves as an identifier type for
 * {@link User} objects. The main reason for its existence is
 * type safety for identifier across the Salespoint Framework. <br>
 * <code>UserIdentifier</code> instances serve as primary key
 * attribute in {@link PersistentUser}, but can also be used as
 * a key for non-persistent, <code>Map</code>-based implementations.
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
