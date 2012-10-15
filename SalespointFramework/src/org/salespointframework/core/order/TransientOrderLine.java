package org.salespointframework.core.order;

import java.util.Objects;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.product.TransientProduct;
import org.salespointframework.core.quantity.MetricMismatchException;
import org.salespointframework.core.quantity.Quantity;

/**
 * 
 * @author Paul Henke
 * 
 */
public class TransientOrderLine implements OrderLine {
	
	private final OrderLineIdentifier orderLineIdentifier = new OrderLineIdentifier();

	private final ProductIdentifier productIdentifier;

	private final Quantity quantity;

	private final Money price;

	private final String productName;
	
	public TransientOrderLine(TransientProduct product, Quantity quantity)
	{
		this.productIdentifier = Objects.requireNonNull(product, "product must be not null").getIdentifier();
		this.quantity = Objects.requireNonNull(quantity, "quantity must be not null");

		if(!product.getMetric().equals(quantity.getMetric())) {
			throw new MetricMismatchException("product.getMetric() is not equal to this.quantity.getMetric()");
		}
		
		this.price = Money.ONE.multiply(product.getPrice());
		this.productName = product.getName();
	}

	@Override
	public final OrderLineIdentifier getIdentifier() {
		return orderLineIdentifier;
	}

	@Override
	public final ProductIdentifier getProductIdentifier() {
		return productIdentifier;
	}


	@Override
	public final Quantity getQuantity() {
		return quantity;
	}

	@Override
	public final Money getPrice() {
		return price;
	}
	
	@Override
	public final String getProductName() {
		return productName;
	}
	
	@Override
	public final boolean equals(Object other)
	{
		if (other == null)
		{
			return false;
		}
		if (other == this)
		{
			return true;
		}
		if (other instanceof TransientOrderLine)
		{
			return this.orderLineIdentifier.equals(((TransientOrderLine)other).orderLineIdentifier);
		}
		return false;
	}


	@Override
	public final int hashCode() {
		return orderLineIdentifier.hashCode();
	}
	
	@Override
	public String toString() {
		return productName + "(" + productIdentifier.toString() + ")" + " - " + quantity;
	}
}
