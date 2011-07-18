package org.salespointframework.core.product.later;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.ProductInstance;
import org.salespointframework.core.quantity.Quantity;

public interface MeasuredProductInstance<T extends MeasuredProductType> extends ProductInstance<T> {

	public T getProductType();
	public Quantity getQuantity();
	public Money getPrice();
}
