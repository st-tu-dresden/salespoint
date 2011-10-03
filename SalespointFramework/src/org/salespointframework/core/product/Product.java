package org.salespointframework.core.product;

import org.salespointframework.core.money.Money;

/**
 * TODO
 * @author Paul Henke
 * 
 */
public interface Product
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
	SerialNumber getSerialNumber();
	
	/**
	 * 
	 * @return the {@link ProductIdentifier} of the {@link ProductType} of the Product
	 */
	ProductIdentifier getProductIdentifier();
	
	/**
	 * 
	 * @return an Iterable of {@link ProductFeature}s of the Product
	 */
	Iterable<ProductFeature> getProductFeatures();
}
