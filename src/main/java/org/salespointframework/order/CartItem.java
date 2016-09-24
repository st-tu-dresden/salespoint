package org.salespointframework.order;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

import javax.money.MonetaryAmount;

import org.salespointframework.catalog.Product;
import org.salespointframework.quantity.Quantity;
import org.springframework.util.Assert;

/**
 * A CartItem consists of a {@link Product} and a {@link Quantity}.
 * 
 * @author Paul Henke
 * @author Oliver Gierke
 */
@ToString
@EqualsAndHashCode
@Getter
public class CartItem implements Priced {

	private final String id = UUID.randomUUID().toString();
	private final MonetaryAmount price;
	private final Quantity quantity;
	private final Product product;

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
		this.price = product.getPrice().multiply(quantity.getAmount());
		this.product = product;
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
	 * Creates an {@link OrderLine} from this CartItem.
	 * 
	 * @return
	 */
	final OrderLine toOrderLine() {
		return new OrderLine(product, quantity);
	}
}
