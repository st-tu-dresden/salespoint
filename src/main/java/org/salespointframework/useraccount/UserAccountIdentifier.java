package org.salespointframework.useraccount;

import java.util.Map;

import javax.persistence.Embeddable;

import org.salespointframework.core.SalespointIdentifier;

/**
 * {@link UserAccountIdentifier} serves as an identifier type for {@link UserAccount} objects. The main reason for its
 * existence is type safety for identifier across the Salespoint Framework. <br />
 * {@link UserAccountIdentifier} instances serve as primary key attribute in {@link UserAccount}, but can also be used
 * as a key for non-persistent, {@link Map}-based implementations.
 * 
 * @author Hannes Weisbach
 * @author Oliver Gierke
 */
@Embeddable
public final class UserAccountIdentifier extends SalespointIdentifier {

	private static final long serialVersionUID = -5156469313158894803L;

	/**
	 * Creates a new unique identifier for {@link UserAccount}s.
	 */
	UserAccountIdentifier() {
		super();
	}

	/**
	 * Creates a new identifier for {@link UserAccount}s. This self defined identifier is not guaranteed to be unique. But
	 * you can provide a human readable value.
	 * 
	 * @param userIdentifier The value of this identifier. Will not be checked to be unique.
	 */
	UserAccountIdentifier(String userIdentifier) {
		super(userIdentifier);
	}
}
