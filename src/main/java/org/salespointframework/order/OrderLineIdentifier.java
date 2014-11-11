package org.salespointframework.order;

import java.util.Map;

import javax.persistence.Embeddable;

import org.salespointframework.core.SalespointIdentifier;

/**
 * {@link OrderLineIdentifier} serves as an identifier type for {@link OrderLine} objects. The main reason for its
 * existence is type safety for identifier across the Salespoint Framework. <br />
 * {@link OrderLineIdentifier} instances serve as primary key attribute in {@link OrderLine}, but can also be used as a
 * key for non-persistent, {@link Map}-based implementations.
 * 
 * @author Thomas Dedek
 * @author Oliver Gierke
 */
@Embeddable
final class OrderLineIdentifier extends SalespointIdentifier {

	private static final long serialVersionUID = 8245571681642563326L;

	/**
	 * Creates a new unique identifier for {@link OrderLine}s
	 */
	OrderLineIdentifier() {
		super();
	}

	/**
	 * Only needed for property editor, shouldn't be used otherwise.
	 * 
	 * @param orderLineIdentifier The string representation of the identifier.
	 */
	OrderLineIdentifier(String orderLineIdentifier) {
		super(orderLineIdentifier);
	}
}
