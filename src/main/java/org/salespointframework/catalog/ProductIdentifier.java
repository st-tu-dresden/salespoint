package org.salespointframework.catalog;

import java.util.Map;

import javax.persistence.Embeddable;

import org.salespointframework.core.SalespointIdentifier;

/**
 * {link ProductIdentifier} serves as an identifier type for {@link Product} objects. The main reason for its existence
 * is type safety for identifier across the Salespoint Framework. <br />
 * {@link ProductIdentifier} instances serve as primary key attribute in {@link Product}, but can also be used as a key
 * for non-persistent, {@link Map}-based implementations.
 * 
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Embeddable
public final class ProductIdentifier extends SalespointIdentifier {

	private static final long serialVersionUID = 7740660930809051850L;

	/**
	 * Creates a new unique identifier for {@link Product}s.
	 */
	ProductIdentifier() {
		super();
	}

	/**
	 * Only needed for property editor, shouldn't be used otherwise.
	 * 
	 * @param productIdentifier The string representation of the identifier.
	 */
	ProductIdentifier(String productIdentifier) {
		super(productIdentifier);
	}
}
