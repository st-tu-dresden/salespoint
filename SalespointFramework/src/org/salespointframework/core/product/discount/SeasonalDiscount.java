package org.salespointframework.core.product.discount;

import org.joda.time.Interval;

@SuppressWarnings("serial")
public class SeasonalDiscount implements Discount {

	public void add(Interval interval, double factor) {
		
	}
	
	public void remove(Interval interval) {
		
	}
	
	@Override
	public double getFactor() {
		// TODO Auto-generated method stub
		return 0;
	}

}
