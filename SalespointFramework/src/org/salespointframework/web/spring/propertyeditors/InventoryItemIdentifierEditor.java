package org.salespointframework.web.spring.propertyeditors;

import java.beans.PropertyEditorSupport;

import org.salespointframework.core.inventory.InventoryItemIdentifier;

/**
 * 
 * @author Paul Henke
 * 
 */
@Deprecated
public class InventoryItemIdentifierEditor extends PropertyEditorSupport
{
	@Override
	public void setAsText(String text)
	{
		@SuppressWarnings("deprecation")
		InventoryItemIdentifier inventoryItemIdentifier = new InventoryItemIdentifier(text);
		setValue(inventoryItemIdentifier);
	}
}
