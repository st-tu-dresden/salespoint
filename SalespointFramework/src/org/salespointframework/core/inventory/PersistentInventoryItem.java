package org.salespointframework.core.inventory;

import java.util.Objects;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.salespointframework.core.catalog.PersistentCatalog;
import org.salespointframework.core.product.PersistentProduct;
import org.salespointframework.core.product.Product;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.quantity.MetricMismatchException;
import org.salespointframework.core.quantity.Quantity;
import org.salespointframework.core.shop.Shop;

@Entity
public class PersistentInventoryItem implements InventoryItem
{
	@EmbeddedId
	private InventoryItemIdentifier inventoryItemIdentifier = new InventoryItemIdentifier();

	//private PersistentProduct product;
	private ProductIdentifier productIdentifier;
	private Quantity quantity;

	@Deprecated
	public PersistentInventoryItem() 
	{
		
	}
	
	public PersistentInventoryItem(PersistentProduct product, Quantity quantity)
	{
		this.productIdentifier = Objects.requireNonNull(product, "product must be not null").getIdentifier();
		this.quantity = Objects.requireNonNull(quantity, "quantity must be not null");
		
		if(!product.getMetric().equals(quantity.getMetric())) 
		{
			throw new MetricMismatchException("product.getMetric is not equal to quantity.getMetric");
		}
	}
	
	@Override
	public final InventoryItemIdentifier getIdentifier()
	{
		return inventoryItemIdentifier;
	}

	@Override
	public final Quantity getQuantity()
	{
		return quantity;
	}

	@Override
	public final Product getProduct()
	{
		PersistentCatalog catalog = (PersistentCatalog) Shop.INSTANCE.getCatalog();
		return catalog.get(PersistentProduct.class, productIdentifier);
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
		if (other instanceof PersistentInventoryItem)
		{
			return this.inventoryItemIdentifier.equals(((PersistentInventoryItem)other).inventoryItemIdentifier);
		}
		return false;
	}
}
