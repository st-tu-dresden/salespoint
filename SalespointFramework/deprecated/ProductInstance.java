package org.salespointframework.core.product;

import org.salespointframework.core.money.Money;


/**
 * The ProductInstance interface. 
 * @author Paul Henke
 * 
 */
public interface ProductInstance
{
	/**
	 * 
	 * @return the {@link Money} value of the ProductInstance
	 */
	Money getPrice();
	/**
	 * 
	 * @return the {@link SerialNumber} of the ProductInstance
	 */
	SerialNumber getSerialNumber();
	
	/**
	 * 
	 * @return the {@link ProductIdentifier} of the {@link Product}, to which the ProductInstance refers 
	 */
	ProductIdentifier getProductIdentifier();
	
	/**
	 * 
	 * @return an {@link Iterable} of {@link ProductFeature}s of the ProductInstance
	 */
	Iterable<ProductFeature> getProductFeatures();
}
