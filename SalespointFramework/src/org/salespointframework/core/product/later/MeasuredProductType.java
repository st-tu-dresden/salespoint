package org.salespointframework.core.product.later;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.ProductType;
import org.salespointframework.core.quantity.Metric;
import org.salespointframework.core.quantity.Quantity;

public interface MeasuredProductType extends ProductType {

	public Quantity getQuantityOnHand();
	public Metric getPreferredMetric();
	public Money getUnitPrice();
}
