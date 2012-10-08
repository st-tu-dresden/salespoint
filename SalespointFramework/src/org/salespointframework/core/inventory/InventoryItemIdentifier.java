package org.salespointframework.core.inventory;

import javax.persistence.Embeddable;

import org.salespointframework.util.SalespointIdentifier;

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
	 * @param serialNumber
	 *            The string representation of the identifier.
	 */
	@Deprecated
	public InventoryItemIdentifier(String inventoryItemIdentifier)
	{
		super(inventoryItemIdentifier);
	}
}
