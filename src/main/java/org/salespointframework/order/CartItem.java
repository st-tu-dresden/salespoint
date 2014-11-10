package org.salespointframework.order;

import java.math.RoundingMode;
import java.util.UUID;

import org.joda.money.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.quantity.Quantity;
import org.springframework.util.Assert;

/**
 * A CartItem consists of a {@link Product} and a {@link Quantity}.
 * 
 * @author Paul Henke
 * @author Oliver Gierke
 */
public class CartItem implements Priced {

	private final Money price;
	private final Quantity quantity;
	private final Product product;
	private final String identifier = UUID.randomUUID().toString();

	/**
	 * Creates a new {@link CartItem}.
	 * 
	 * @param product must not be {@literal null}.
	 * @param quantity must not be {@literal null}.
	 */
	CartItem(Product product, Quantity quantity) {

		Assert.notNull(product, "Product must be not null!");
		Assert.notNull(quantity, "Quantity must be not null!");

		product.verify(quantity);

		this.quantity = quantity;
		this.price = product.getPrice().multipliedBy(quantity.getAmount(), RoundingMode.HALF_UP);
		this.product = product;
	}

	/**
	 * Returns the identifier of the {@link CartItem}.
	 * 
	 * @return
	 */
	public final String getIdentifier() {
		return identifier;
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.Priced#getPrice()
	 */
	public final Money getPrice() {
		return price;
	}

	/**
	 * Returns the {@link Product} associated with the {@link CartItem}.
	 * 
	 * @return
	 */
	public final Product getProduct() {
		return product;
	}

	/**
	 * Returns the name of the {@link Product} associated with the {@link CartItem}.
	 * 
	 * @return
	 */
	public final String getProductName() {
		return product.getName();
	}

	/**
	 * Returns the {@link Quantity} of the {@link CartItem}.
	 * 
	 * @return
	 */
	public final Quantity getQuantity() {
		return quantity;
	};

	/**
	 * Creates an {@link OrderLine} from this CartItem.
	 * 
	 * @return
	 */
	final OrderLine toOrderLine() {
		return new OrderLine(product, quantity);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {

		if (other == this) {
			return true;
		}

		if (!(other instanceof CartItem)) {
			return false;
		}

		CartItem that = (CartItem) other;

		return this.product.equals(that.getProduct()) && this.quantity.equals(that.getQuantity());
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return product.hashCode() ^ quantity.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getProductName() + " - " + quantity;
	}
}
