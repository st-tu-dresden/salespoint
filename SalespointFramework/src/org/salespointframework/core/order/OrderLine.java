package org.salespointframework.core.order;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.ProductFeature;
import org.salespointframework.core.product.ProductIdentifier;

/**
 * 
 * @author Thomas Dedek
 * @author Paul Henke
 * 
 */
public interface OrderLine
{
	OrderLineIdentifier getOrderLineIdentifier();
	ProductIdentifier getProductIdentifier();
	Iterable<ProductFeature> getProductFeatures();
	int getNumberOrdered();
	Money getPrice();
}
