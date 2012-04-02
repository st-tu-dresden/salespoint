package org.salespointframework.core.order;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.ProductFeature;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.product.Product;

// TODO convert to embedabble class!?
/**
 * 
 * @author Thomas Dedek
 * @author Paul Henke
 * 
 */
public interface OrderLine
{
	/**
	 * 
	 * @return the {@link OrderLineIdentifier} to uniquely identify this orderline
	 */
	OrderLineIdentifier getIdentifier();
	/**
	 * 
	 * @return the {@link ProductIdentifier} of this orderline 
	 */
	ProductIdentifier getProductIdentifier();
	/**
	 * 
	 * @return an Iterable of {@link ProductFeature}s from this orderline
	 */
	Iterable<ProductFeature> getProductFeatures();
	/**
	 * 
	 * @return the number of ordered {@link Product}s
	 */
	int getNumberOrdered();
	/**
	 * 
	 * @return the value of the orderline
	 */
	Money getPrice();
}
