package org.salespointframework.core.product.discount;

@SuppressWarnings("serial")
public class NoDiscount implements Discount {

	@Override
	public double getFactor() {
		return 1;
	}

}
