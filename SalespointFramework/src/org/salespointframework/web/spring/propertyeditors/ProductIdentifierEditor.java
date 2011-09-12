package org.salespointframework.web.spring.propertyeditors;

import java.beans.PropertyEditorSupport;

import org.salespointframework.core.product.ProductIdentifier;

/**
 * 
 * @author Paul Henke
 * 
 */
public class ProductIdentifierEditor extends PropertyEditorSupport {
	@Override
	public void setAsText(String text) {
		ProductIdentifier productIdentifier = new ProductIdentifier(text);
		setValue(productIdentifier);
	}
}