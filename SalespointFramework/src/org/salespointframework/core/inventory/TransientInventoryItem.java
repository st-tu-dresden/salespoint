package org.salespointframework.core.inventory;

import java.util.Objects;

import org.salespointframework.core.product.Product;
import org.salespointframework.core.product.TransientProduct;
import org.salespointframework.core.quantity.MetricMismatchException;
import org.salespointframework.core.quantity.Quantity;

public class TransientInventoryItem implements InventoryItem
{

	private InventoryItemIdentifier inventoryItemIdentifier = new InventoryItemIdentifier();

	private Product product;

	private Quantity quantity;

	@Override
	public InventoryItemIdentifier getIdentifier()
	{
		return inventoryItemIdentifier;
	}

	@Override
	public Product getProduct()
	{
		return product;
	}

	@Override
	public Quantity getQuantity()
	{
		return quantity;
	}

	public TransientInventoryItem(TransientProduct product, Quantity quantity)
	{
		this.product = Objects.requireNonNull(product, "product must be not null");
		this.quantity = Objects.requireNonNull(quantity, "quantity must be not null");
		
		if(!product.getMetric().equals(quantity.getMetric())) 
		{
			throw new MetricMismatchException("product.getMetric is not equal to quantity.getMetric");
		}
	}

	@Override
	public void decreaseQuantity(Quantity quantity)
	{
		Objects.requireNonNull(quantity, "quantity must not be null");
		if(!this.getProduct().getMetric().equals(quantity.getMetric()))
{
			throw new MetricMismatchException("this.product.getMetric is not equal to quantity.getMetric");
		}
		
		this.quantity = this.quantity.subtract(quantity); // TODO negative check? -> Exception?
	}

	@Override
	public void increaseQuantiy(Quantity quantity)
	{
		Objects.requireNonNull(quantity, "quantity must not be null");
		if(!this.getProduct().getMetric().equals(quantity.getMetric())) 
		{
			throw new MetricMismatchException("this.product.getMetric is not equal to quantity.getMetric");
		}
		this.quantity = this.quantity.add(quantity);
	}
	
	@Override
	public final int hashCode() 
	{
		return inventoryItemIdentifier.hashCode();
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
		if (other instanceof TransientInventoryItem)
		{
			return this.inventoryItemIdentifier.equals(((TransientInventoryItem)other).inventoryItemIdentifier);
		}
		return false;
	}
}
