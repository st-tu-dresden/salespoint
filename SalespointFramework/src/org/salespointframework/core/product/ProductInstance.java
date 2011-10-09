package org.salespointframework.core.product;

import org.salespointframework.core.money.Money;

/**
 * TODO
 * @author Paul Henke
 * 
 */
public interface ProductInstance
{
	/**
	 * 
	 * @return the value of the Product
	 */
	Money getPrice();
	/**
	 * 
	 * @return the unique {@link SerialNumber} of the Product
	 */
	SerialNumber getIdentifier();
	
	/**
	 * 
	 * @return the {@link ProductTypeIdentifier} of the {@link ProductType} of the Product
	 */
	ProductTypeIdentifier getProductTypeIdentifier();
	
	/**
	 * 
	 * @return an Iterable of {@link ProductFeature}s of the Product
	 */
	Iterable<ProductFeature> getProductFeatures();
}
