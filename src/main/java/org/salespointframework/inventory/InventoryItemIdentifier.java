package org.salespointframework.inventory;

import javax.persistence.Embeddable;

import org.salespointframework.core.SalespointIdentifier;

/**
 * {@code InventoryItemIdentifier} serves as an identifier type for {@link InventoryItem} objects. The main reason for
 * its existence is type safety for identifier across the Salespoint Framework. <br />
 * {@code InventoryItemIdentifier} instances serve as primary key attribute in {@link InventoryItem}, but can also be
 * used as a key for non-persistent, <code>Map</code>-based implementations.
 * 
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Embeddable
public class InventoryItemIdentifier extends SalespointIdentifier {

	private static final long serialVersionUID = -5195493076944614L;

	/**
	 * Creates a new unique identifier for {@link InventoryItem}s.
	 */
	InventoryItemIdentifier() {
		super();
	}

	/**
	 * Only needed for property editor, shouldn't be used otherwise.
	 * 
	 * @param inventoryItemIdentifier The string representation of the identifier.
	 */
	InventoryItemIdentifier(String inventoryItemIdentifier) {
		super(inventoryItemIdentifier);
	}
}
