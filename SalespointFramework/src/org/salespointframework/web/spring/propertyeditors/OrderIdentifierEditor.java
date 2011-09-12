package org.salespointframework.web.spring.propertyeditors;

import java.beans.PropertyEditorSupport;

import org.salespointframework.core.order.OrderIdentifier;

/**
 * 
 * @author Paul Henke
 * 
 */
public class OrderIdentifierEditor extends PropertyEditorSupport {
	@Override
	public void setAsText(String text) {
		OrderIdentifier orderIdentifier = new OrderIdentifier(text);
		setValue(orderIdentifier);
	}
}