package org.salespointframework.order;

import java.math.RoundingMode;
import java.util.UUID;

import org.joda.money.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.quantity.MetricMismatchException;
import org.salespointframework.quantity.Quantity;
import org.springframework.util.Assert;

/**
 * A CartItem consists of a {@link Product} and a {@link Quantity}.
 * 
 * @author Paul Henke
 *
 */
public class CartItem implements Priced {
	
	private final Money price;
	private final Quantity quantity;
	private final Product product;
	private final String identifier = UUID.randomUUID().toString();
	
	/**
	 * Creates a new CartItem.
	 * @param product must not be {@literal null}.
	 * @param quantity must not be {@literal null}.
	 */
	CartItem(Product product, Quantity quantity) {

		Assert.notNull(product, "Product must be not null!");
		Assert.notNull(quantity, "Quantity must be not null!");

		if (!product.supports(quantity)) {
			throw new MetricMismatchException(String.format("Product %s does not support quantity %s!", product, quantity));
		}

		this.quantity = quantity;
		this.price = product.getPrice().multipliedBy(quantity.getAmount(), RoundingMode.HALF_UP);
		this.product = product;
	}
	
	@Override public boolean equals(Object other) {
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof CartItem))return false;
	    CartItem otherCartItem = (CartItem)other;
	    return this.product.equals(otherCartItem.getProduct()) && this.quantity.equals(otherCartItem.getQuantity());
	}
	
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

	public final Product getProduct() {
		return product;
	}
	
	public final String getProductName() {
		return product.getName();
	}
	
	public final Quantity getQuantity() {
		return quantity;
	};
	
	@Override public int hashCode() {
		return product.hashCode() ^ quantity.hashCode();
	}
	
	/**
	 * Creates an {@link OrderLine} from this CartItem.
	 * @return
	 */
	public final OrderLine toOrderline() {
		return new OrderLine(product, quantity);
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
