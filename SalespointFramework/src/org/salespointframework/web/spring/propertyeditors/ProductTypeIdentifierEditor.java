package org.salespointframework.web.spring.propertyeditors;

import java.beans.PropertyEditorSupport;

import org.salespointframework.core.product.ProductTypeIdentifier;

/**
 * 
 * @author Paul Henke
 * 
 */
public class ProductTypeIdentifierEditor extends PropertyEditorSupport
{
	@Override
	public void setAsText(String text)
	{
		@SuppressWarnings("deprecation")
		ProductTypeIdentifier productTypeIdentifier = new ProductTypeIdentifier(text);
		setValue(productTypeIdentifier);
	}
}