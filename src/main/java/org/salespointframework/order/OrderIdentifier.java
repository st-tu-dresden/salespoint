package org.salespointframework.order;

import java.util.Map;

import javax.persistence.Embeddable;

import org.salespointframework.core.SalespointIdentifier;

/**
 * {@link OrderIdentifier serves as an identifier type for {@link Order} objects. The main reason for its existence is
 * type safety for identifier across the Salespoint Framework. <br />
 * {@link OrderIdentifier} instances serve as primary key attribute in {@link Order}, but can also be used as a key for
 * non-persistent, {@link Map}-based implementations.
 * 
 * @author Thomas Dedek
 */
@Embeddable
public final class OrderIdentifier extends SalespointIdentifier {

	private static final long serialVersionUID = -5109304499529387062L;

	/**
	 * Creates a new unique identifier for {@link Order}.
	 */
	OrderIdentifier() {
		super();
	}

	/**
	 * Only needed for property editor, shouldn't be used otherwise.
	 * 
	 * @param orderIdentifier The string representation of the identifier.
	 */
	OrderIdentifier(String orderIdentifier) {
		super(orderIdentifier);
	}
}
