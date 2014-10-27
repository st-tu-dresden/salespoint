package org.salespointframework.order;

import java.math.RoundingMode;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.core.AbstractEntity;
import org.salespointframework.quantity.MetricMismatchException;
import org.salespointframework.quantity.Quantity;
import org.springframework.util.Assert;

/**
 * An order line
 * 
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Entity
public class OrderLine extends AbstractEntity<OrderLineIdentifier> implements Priced {

	private @EmbeddedId @AttributeOverride(name = "id", column = @Column(name = "ORDERLINE_ID"))//
	OrderLineIdentifier orderLineIdentifier = new OrderLineIdentifier();

	private @Embedded @AttributeOverride(name = "id", column = @Column(name = "PRODUCT_ID"))//
	ProductIdentifier productIdentifier;

	private @Lob Money price = Money.of(CurrencyUnit.EUR, 1.0);
	private @Lob Quantity quantity;
	private String productName;

	/**
	 * Parameterless constructor required for JPA. Do not use.
	 */
	@Deprecated
	protected OrderLine() {}

	/**
	 * Creates a new {@link OrderLine} for the given {@link Product} and {@link Quantity}.
	 * 
	 * @param product must not be {@literal null}.
	 * @param quantity must not be {@literal null}.
	 */
	public OrderLine(Product product, Quantity quantity) {

		Assert.notNull(product, "Product must be not null!");
		Assert.notNull(quantity, "Quantity must be not null!");

		if (!product.supports(quantity)) {
			throw new MetricMismatchException(String.format("Product %s does not support quantity %s!", product, quantity));
		}

		this.productIdentifier = product.getIdentifier();
		this.quantity = quantity;
		this.price = product.getPrice().multipliedBy(quantity.getAmount(), RoundingMode.HALF_UP);
		this.productName = product.getName();
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.core.AbstractEntity#getIdentifier()
	 */
	public final OrderLineIdentifier getIdentifier() {
		return orderLineIdentifier;
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.Priced#getPrice()
	 */
	public final Money getPrice() {
		return price;
	}

	public final ProductIdentifier getProductIdentifier() {
		return productIdentifier;
	}

	public final String getProductName() {
		return productName;
	}

	public final Quantity getQuantity() {
		return quantity;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return productName + "(" + productIdentifier.toString() + ")" + " - " + quantity;
	}
}
