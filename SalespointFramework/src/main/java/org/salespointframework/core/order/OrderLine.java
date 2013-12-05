package org.salespointframework.core.order;

import java.util.Objects;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;

import org.salespointframework.core.AbstractEntity;
import org.salespointframework.core.catalog.Product;
import org.salespointframework.core.catalog.ProductIdentifier;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.quantity.MetricMismatchException;
import org.salespointframework.core.quantity.Quantity;


// TODO comments
/**
 * This class is immutable.
 * 
 * @author Paul Henke
 *
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
	private Money price = Money.ONE;

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
		
		this.price = quantity.multiply(product.getPrice());
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
