package org.salespointframework.web.spring.propertyeditors;

import java.beans.PropertyEditorSupport;

import org.salespointframework.core.order.OrderLineIdentifier;

/**
 * 
 * @author Paul Henke
 * 
 */
public class OrderLineIdentifierEditor extends PropertyEditorSupport
{
	@Override
	public void setAsText(String text)
	{
		@SuppressWarnings("deprecation")
		OrderLineIdentifier orderLineIdentifier = new OrderLineIdentifier(text);
		setValue(orderLineIdentifier);
	}
}