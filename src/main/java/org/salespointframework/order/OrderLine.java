package org.salespointframework.order;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.money.MonetaryAmount;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

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
@ToString
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE, onConstructor = @__(@Deprecated) )
public class OrderLine extends AbstractEntity<OrderLineIdentifier> implements Priced {

	private static final long serialVersionUID = -4310089726057038893L;

	@EmbeddedId //
	@AttributeOverride(name = "id", column = @Column(name = "ORDERLINE_ID") ) //
	private OrderLineIdentifier orderLineIdentifier = new OrderLineIdentifier();

	@Embedded //
	@AttributeOverride(name = "id", column = @Column(name = "PRODUCT_ID") ) //
	private @Getter ProductIdentifier productIdentifier;

	private @Getter MonetaryAmount price;
	private @Getter Quantity quantity;
	private @Getter String productName;

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
		this.price = product.getPrice().multiply(quantity.getAmount());
		this.productName = product.getName();
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.core.AbstractEntity#getIdentifier()
	 */
	public final OrderLineIdentifier getIdentifier() {
		return orderLineIdentifier;
	}
}
