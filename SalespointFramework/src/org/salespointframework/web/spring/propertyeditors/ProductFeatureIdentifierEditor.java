package org.salespointframework.web.spring.propertyeditors;

import java.beans.PropertyEditorSupport;

import org.salespointframework.core.product.ProductIdentifier;

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
		ProductIdentifier productFeatureIdenfitier = new ProductIdentifier(text);
		setValue(productFeatureIdenfitier);
	}
}
