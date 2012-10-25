package org.salespointframework.core.product.discount;

import org.salespointframework.core.quantity.Quantity;

@SuppressWarnings("serial")
public class VolumeDiscount implements Discount {

	public void add(Quantity minQuantity, double factor) {
		
	}
	
	public void remove(Quantity minQuantity) {
		
	}
	
	/* TODO Map<Quantity, double> oder was sinnvolleres returnen */
	public Object  getFoobar() {
		return "";
	}
	
	@Override
	public double getFactor() {
		// TODO Auto-generated method stub
		return 0;
	}

}
