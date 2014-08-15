package org.salespointframework.inventory;

import java.util.Objects;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import org.salespointframework.catalog.Product;
import org.salespointframework.core.AbstractEntity;
import org.salespointframework.quantity.MetricMismatchException;
import org.salespointframework.quantity.Quantity;

/**
 * 
 * @author Paul Henke
 *
 */
@Entity
public class InventoryItem extends AbstractEntity<InventoryItemIdentifier>
{
	@EmbeddedId
	@AttributeOverride(name = "id", column = @Column(name = "ITEM_ID"))
	private InventoryItemIdentifier inventoryItemIdentifier = new InventoryItemIdentifier();

	@OneToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE, /* CascadeType.REMOVE,*/ CascadeType.REFRESH, CascadeType.DETACH})
	private Product product;
	
	@Lob
	private Quantity quantity;

	@Deprecated
	protected InventoryItem() { }
	
	/**
	 * Creates a new InventoryItem
	 * @param product the {@link Product} for this InventoryItem
	 * @param quantity the initial {@link Quantity} for this InventoryItem
	 */
	public InventoryItem(Product product, Quantity quantity)
	{
		this.product = Objects.requireNonNull(product, "product must be not null");
		this.quantity = Objects.requireNonNull(quantity, "quantity must be not null");
		
		if(!product.getMetric().equals(quantity.getMetric())) 
		{
			throw new MetricMismatchException("product.getMetric() is not equal to this.quantity.getMetric()", product.getMetric(), this.quantity.getMetric());
		}
	}
	
	public final InventoryItemIdentifier getIdentifier() 
	{
		return inventoryItemIdentifier;
	}

	public final Quantity getQuantity()
	{
		return quantity;
	}

	public final Product getProduct()
	{
		return product;
	}

	public void decreaseQuantity(Quantity quantity)
	{
		Objects.requireNonNull(quantity, "quantity must not be null");
		if(!this.getProduct().getMetric().equals(quantity.getMetric())) 
		{
			throw new MetricMismatchException("this.product.getMetric is not equal to quantity.getMetric");
		}
		
		this.quantity = this.quantity.subtract(quantity); // TODO negative check? -> Exception?
	}

	public void increaseQuantity(Quantity quantity)
	{
		Objects.requireNonNull(quantity, "quantity must not be null");
		if(!this.getProduct().getMetric().equals(quantity.getMetric())) 
		{
			throw new MetricMismatchException("this.product.getMetric is not equal to quantity.getMetric");
		}
		this.quantity = this.quantity.add(quantity);
	}
}
