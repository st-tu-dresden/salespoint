package org.salespointframework.core.order;

import java.util.Objects;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.salespointframework.core.catalog.Catalog;
import org.salespointframework.core.catalog.PersistentCatalog;
import org.salespointframework.core.catalog.TransientCatalog;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.PersistentProduct;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.quantity.MetricMismatchException;
import org.salespointframework.core.quantity.Quantity;
import org.salespointframework.core.shop.Shop;

/**
 * A persistent implementation of the {@link OrderLine} interface.
 * This class is immutable.
 * 
 * @author Paul Henke
 *
 */
@Entity
public class PersistentOrderLine implements OrderLine
{
	@EmbeddedId
	@AttributeOverride(name = "id", column = @Column(name = "ORDERLINE_ID"))
	private OrderLineIdentifier orderLineIdentifier = new OrderLineIdentifier();

	@Embedded
	@AttributeOverride(name = "id", column = @Column(name = "PRODUCT_ID"))
	private ProductIdentifier productIdentifier;


	private Money price = Money.ONE;

	private String productName;

	private Quantity quantity;

	/**
	 * Parameterless constructor required for JPA. Do not use.
	 */
	@Deprecated
	protected PersistentOrderLine() { }
	
	public PersistentOrderLine(PersistentProduct product, Quantity quantity)
	{
		this.productIdentifier = Objects.requireNonNull(product, "product must be not null").getIdentifier();
		this.quantity = Objects.requireNonNull(quantity, "quantity must be not null");
		
		if(!product.getMetric().equals(quantity.getMetric())) {
			throw new MetricMismatchException("product.getMetric() is not equal to this.quantity.getMetric()");
		}
		
		this.price = quantity.multiply(product.getPrice());
		this.productName = product.getName();
	}


	@Override
	public final OrderLineIdentifier getIdentifier()
	{
		return orderLineIdentifier;
	}

	@Override
	public final Money getPrice()
	{
		return price;
	}

	@Override
	public final ProductIdentifier getProductIdentifier()
	{
		return productIdentifier;
	}

	@Override
	public final String getProductName()
	{
		return productName;
	}

	@Override
	public final Quantity getQuantity()
	{
		return quantity;
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
		if (other instanceof PersistentOrderLine)
		{
			return this.orderLineIdentifier.equals(((PersistentOrderLine)other).orderLineIdentifier);
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
