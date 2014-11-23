package org.salespointframework.useraccount;

import javax.persistence.Embeddable;

import org.salespointframework.core.SalespointIdentifier;

/**
 * {@code UserAccountIdentifier} serves as an identifier type for {@link UserAccount} objects. The main reason for its
 * existence is type safety for identifier across the Salespoint Framework. <br />
 * {@code UserIdentifier} instances serve as primary key attribute in {@link UserAccount}, but can also be used as a key
 * for non-persistent, {@code Map}-based implementations.
 * 
 * @author Hannes Weisbach
 * @author Oliver Gierke
 */
@Embeddable
public final class UserAccountIdentifier extends SalespointIdentifier {

	private static final long serialVersionUID = 4211737659273261710L;

	/**
	 * Creates a new unique identifier for {@link UserAccount}s.
	 */
	public UserAccountIdentifier() {
		super();
	}

	/**
	 * Creates a new identifier for {@link UserAccount}s. This self defined identifier is not guaranteed to be unique. But
	 * you can provide a human readable value.
	 * 
	 * @param userIdentifier The value of this identifier. Will not be checked to be unique.
	 */
	public UserAccountIdentifier(String userIdentifier) {
		super(userIdentifier);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.core.SalespointIdentifier#hashCode()
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.core.SalespointIdentifier#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		return super.equals(other);
	}
}
