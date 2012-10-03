package org.salespointframework.core.product;

import java.math.RoundingMode;

import org.salespointframework.core.quantity.Quantity;
import org.salespointframework.core.quantity.rounding.BasicRoundingStrategy;

class TransientProductInstance {
	
	private SerialNumber serialNumber = new SerialNumber();

	private Product product;
	
	private Quantity quantity;
	
	public SerialNumber getSerialNumber() {
		return serialNumber;
	}

	public Product getProduct() {
		return product;
	}

	public Quantity getQuantity() {
		return quantity;
	}

	public TransientProductInstance(TransientProduct product) {
		this(product, new Quantity(0, product.getMetric(), new BasicRoundingStrategy(10, RoundingMode.CEILING)));
	}
	
	public TransientProductInstance(TransientProduct product, Quantity quantity) {
		if(!product.getMetric().equals(quantity.getMetric())) throw new RuntimeException(); //todo exception
		this.product = product;
		this.quantity = quantity;
	}
	
	public void decreaseQuantity(Quantity quantity) {
		this.quantity = this.quantity.subtract(quantity);	//TODO negative
	}
	
	public void increaseQuantiy(Quantity quantity) 
	{
		this.quantity = this.quantity.add(quantity);
	}
}
