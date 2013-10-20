package org.salespointframework.core.useraccount;

import javax.persistence.Embeddable;

import org.salespointframework.core.SalespointIdentifier;

/**
 * <code>UserAccountIdentifier</code> serves as an identifier type for {@link UserAccount}
 * objects. The main reason for its existence is type safety for identifier
 * across the Salespoint Framework. <br>
 * <code>UserIdentifier</code> instances serve as primary key attribute in
 * {@link UserAccount}, but can also be used as a key for non-persistent,
 * <code>Map</code>-based implementations.
 * 
 * @author Hannes Weisbach
 */
@SuppressWarnings("serial")
@Embeddable
public final class UserAccountIdentifier extends SalespointIdentifier {
    /**
     * Creates a new unique identifier for {@link UserAccount}s.
     */
    public UserAccountIdentifier() {
        super();
    }

    /**
     * Creates a new identifier for {@link UserAccount}s. This self defined identifier
     * is not guaranteed to be unique. But you can provide a human readable
     * value.
     * 
     * @param userIdentifier The value of this identifier. Will not be checked to be unique.
     */
    public UserAccountIdentifier(String userIdentifier) {
        super(userIdentifier);
    }
    
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object other) { 
		return super.equals(other);
	}
}
