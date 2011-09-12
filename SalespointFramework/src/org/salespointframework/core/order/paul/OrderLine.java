package org.salespointframework.core.order.paul;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.order.OrderLineIdentifier;
import org.salespointframework.core.product.ProductFeature;
import org.salespointframework.core.product.ProductType;

/**
 * 
 * @author Thomas Dedek
 * @author Paul Henke
 *
 */
public interface OrderLine {

	OrderLineIdentifier getOrderLineIdentifier();
	ProductType getProductType();
	Iterable<ProductFeature> getProductFeatures();
	int getNumberOrdered();
	Money getPrice();
}
