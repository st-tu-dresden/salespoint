package org.salespointframework.inventory;

import javax.persistence.Embeddable;

import org.salespointframework.core.SalespointIdentifier;

/**
 * {@code InventoryItemIdentifier} serves as an identifier type for {@link InventoryItem}
 * objects. The main reason for its existence is type safety for identifier
 * across the Salespoint Framework. <br />
 * {@code InventoryItemIdentifier} instances serve as primary key attribute in
 * {@link InventoryItem}, but can also be used as a key for non-persistent,
 * <code>Map</code>-based implementations.
 * 
 * @author Paul Henke
 * 
 */
@SuppressWarnings("serial")
@Embeddable
public class InventoryItemIdentifier extends SalespointIdentifier
{
	/**
	 * Creates a new unique identifier for {@link InventoryItem}s.
	 */
	public InventoryItemIdentifier()
	{
		super();
	}

	/**
	 * Only needed for property editor, shouldn't be used otherwise.
	 * 
	 * @param inventoryItemIdentifier
	 *            The string representation of the identifier.
	 */
	@Deprecated
	public InventoryItemIdentifier(String inventoryItemIdentifier)
	{
		super(inventoryItemIdentifier);
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
