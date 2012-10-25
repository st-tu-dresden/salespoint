package org.salespointframework.core.product.discount;

import java.util.HashSet;
import java.util.Set;

import org.salespointframework.util.Iterables;

@SuppressWarnings("serial")
public class DiscountGroup implements Discount {

	private Set<Discount> discountSet = new HashSet<Discount>();
	
	public void add(Discount discount) {
		discountSet.add(discount);
	}
	
	public void remove(Discount discount) {
		discountSet.remove(discount);
	}
	
	public Iterable<Discount> getDiscounts() {
		return Iterables.of(discountSet);
	}

	@Override
	public double getFactor() {
		double factor = 1;
		for(Discount d : discountSet) {
			factor *= d.getFactor();
		}
		return factor;
	}
}
