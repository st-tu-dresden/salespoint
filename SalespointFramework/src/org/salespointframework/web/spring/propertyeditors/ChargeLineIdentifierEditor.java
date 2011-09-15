package org.salespointframework.web.spring.propertyeditors;

import java.beans.PropertyEditorSupport;

import org.salespointframework.core.order.ChargeLineIdentifier;

/**
 * 
 * @author Paul Henke
 * 
 */
public class ChargeLineIdentifierEditor extends PropertyEditorSupport {
	@Override
	public void setAsText(String text) {
		ChargeLineIdentifier chargeLineIdentifier = new ChargeLineIdentifier(text);
		setValue(chargeLineIdentifier);
	}
}