package org.salespointframework.core.accountancy;

import javax.persistence.Embeddable;

import org.salespointframework.util.SalespointIdentifier;

/**
 * <code>AccountancyEntryIdentifier</code> serves as an identifier type for
 * <code>AccountancyEntry</code> objects. The main reason for its existence is
 * type safety for identifier across the Salespoint Framework. <br>
 * <code>AccoutancyEntryIdentifier</code> instances serve as primary key
 * attribute in <code>PersistentAccountancyEntry</code>, but can also be used as
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
	 * @param id The string representation of the identifier. 
	 */
	@Deprecated
	public AccountancyEntryIdentifier(String id) {
		super(id);
	}
}
