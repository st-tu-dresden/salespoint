package org.salespointframework.web.spring.propertyeditors;

import java.beans.PropertyEditorSupport;

import org.salespointframework.core.product.ProductFeatureIdentifier;

/**
 * 
 * @author Paul Henke
 *
 */
public class ProductFeatureIdentifierEditor extends PropertyEditorSupport 
{
	@Override
	public void setAsText(String text)
	{
		@SuppressWarnings("deprecation")
		ProductFeatureIdentifier productFeatureIdenfitier = new ProductFeatureIdentifier(text);
		setValue(productFeatureIdenfitier);
	}
}
