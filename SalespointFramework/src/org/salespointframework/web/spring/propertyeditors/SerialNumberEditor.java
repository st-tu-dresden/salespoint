package org.salespointframework.web.spring.propertyeditors;

import java.beans.PropertyEditorSupport;

import org.salespointframework.core.product.SerialNumber;

/**
 * 
 * @author Paul Henke
 * 
 */
public class SerialNumberEditor extends PropertyEditorSupport {
	@Override
	public void setAsText(String text) {
		SerialNumber serialNumber = new SerialNumber(text);
		setValue(serialNumber);
	}
}
