package org.salespointframework.core.accountancy;

import javax.persistence.Embeddable;

import org.salespointframework.core.SalespointIdentifier;

/**
 * <code>AccountancyEntryIdentifier</code> serves as an identifier type for
 * {@link AccountancyEntry} objects. The main reason for its existence is
 * type safety for identifier across the Salespoint Framework. <br>
 * <code>AccoutancyEntryIdentifier</code> instances serve as primary key
 * attribute in {@link PersistentAccountancyEntry}, but can also be used as
 * a key for non-persistent, <code>Map</code>-based implementations.
 * 
 * @author Hannes Weisbach
 * 
 */
@SuppressWarnings("serial")
@Embeddable
public final class AccountancyEntryIdentifier extends SalespointIdentifier {

	/**
	 * Creates a new unique identifier for accountancy entries.
	 */
	public AccountancyEntryIdentifier() {
		super();
	}

	/**
	 * Only needed for property editor, shouldn't be used otherwise.
	 * 
	 * @param identifier
	 *            The string representation of the identifier.
	 */
	@Deprecated
	public AccountancyEntryIdentifier(String identifier) {
		super(identifier);
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
