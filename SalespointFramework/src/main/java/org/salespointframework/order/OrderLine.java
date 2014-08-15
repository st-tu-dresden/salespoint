package org.salespointframework.order;

import java.math.RoundingMode;
import java.util.Objects;

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

/**
 * An order line
 * 
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Entity
public class OrderLine extends AbstractEntity<OrderLineIdentifier>
{
	@EmbeddedId
	@AttributeOverride(name = "id", column = @Column(name = "ORDERLINE_ID"))
	private OrderLineIdentifier orderLineIdentifier = new OrderLineIdentifier();

	@Embedded
	@AttributeOverride(name = "id", column = @Column(name = "PRODUCT_ID"))
	private ProductIdentifier productIdentifier;

	@Lob
	private Money price = Money.of(CurrencyUnit.EUR, 1.0);

	private String productName;

	@Lob
	private Quantity quantity;

	/**
	 * Parameterless constructor required for JPA. Do not use.
	 */
	@Deprecated
	protected OrderLine() { }
	
	public OrderLine(Product product, Quantity quantity)
	{
		this.productIdentifier = Objects.requireNonNull(product, "product must be not null").getIdentifier();
		this.quantity = Objects.requireNonNull(quantity, "quantity must be not null");
		
		if(!product.getMetric().equals(quantity.getMetric())) {
			throw new MetricMismatchException("product.getMetric() is not equal to this.quantity.getMetric()");
		}
		
		this.price = product.getPrice().multipliedBy(quantity.getAmount(), RoundingMode.HALF_UP);
		this.productName = product.getName();
	}


	public final OrderLineIdentifier getIdentifier()
	{
		return orderLineIdentifier;
	}

	public final Money getPrice()
	{
		return price;
	}

	public final ProductIdentifier getProductIdentifier()
	{
		return productIdentifier;
	}

	public final String getProductName()
	{
		return productName;
	}

	public final Quantity getQuantity()
	{
		return quantity;
	}

	@Override
	public String toString() {
		return productName + "(" + productIdentifier.toString() + ")" + " - " + quantity;
	}
}
