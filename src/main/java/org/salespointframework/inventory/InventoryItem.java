package org.salespointframework.inventory;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import org.salespointframework.catalog.Product;
import org.salespointframework.core.AbstractEntity;
import org.salespointframework.quantity.Quantity;
import org.springframework.util.Assert;

/**
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Entity
public class InventoryItem extends AbstractEntity<InventoryItemIdentifier> {

	@EmbeddedId//
	@AttributeOverride(name = "id", column = @Column(name = "ITEM_ID"))//
	private final InventoryItemIdentifier inventoryItemIdentifier = new InventoryItemIdentifier();

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH })//
	private Product product;

	private @Lob Quantity quantity;

	@Deprecated
	protected InventoryItem() {}

	/**
	 * Creates a new {@link InventoryItem} for the given {@link Product} and {@link Quantity}.
	 * 
	 * @param product the {@link Product} for this {@link InventoryItem}, must not be {@literal null}.
	 * @param quantity the initial {@link Quantity} for this {@link InventoryItem}, must not be {@literal null}.
	 */
	public InventoryItem(Product product, Quantity quantity) {

		Assert.notNull(product, "Product must be not null!");
		Assert.notNull(quantity, "Quantity must be not null!");

		product.verify(quantity);

		this.product = product;
		this.quantity = quantity;

	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.core.AbstractEntity#getIdentifier()
	 */
	public final InventoryItemIdentifier getIdentifier() {
		return inventoryItemIdentifier;
	}

	public final Quantity getQuantity() {
		return quantity;
	}

	public final Product getProduct() {
		return product;
	}

	/**
	 * Returns whether the {@link InventoryItem} is available in exactly or more of the given quantity.
	 * 
	 * @param quantity must not be {@literal null}.
	 * @return
	 */
	public boolean hasSufficientQuantity(Quantity quantity) {
		return !this.quantity.subtract(quantity).isNegative();
	}

	public void decreaseQuantity(Quantity quantity) {

		Assert.notNull(quantity, "Quantity must not be null!");
		Assert.isTrue(this.quantity.isGreaterThanOrEqualTo(quantity),
				String.format("Insufficient quantity! Have %s but was requested to reduce by %s.", this.quantity, quantity));

		product.verify(quantity);

		this.quantity = quantity.subtract(quantity);
	}

	public void increaseQuantity(Quantity quantity) {

		Assert.notNull(quantity, "Quantity must not be null!");
		product.verify(quantity);

		this.quantity = quantity.add(quantity);
	}
}
