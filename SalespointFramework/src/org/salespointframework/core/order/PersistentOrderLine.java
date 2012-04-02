package org.salespointframework.core.order;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.salespointframework.core.database.Database;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.PersistentProduct;
import org.salespointframework.core.product.Product;
import org.salespointframework.core.product.ProductFeature;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.product.ProductInstance;
import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;

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

	@ElementCollection
	private Set<ProductFeature> productFeatures = new HashSet<ProductFeature>();

	private int numberOrdered;

	private Money price = Money.ZERO;

	private String productName;

	/**
	 * Parameterless constructor required for JPA. Do not use.
	 */
	@Deprecated
	protected PersistentOrderLine()
	{

	}
	
	/**
	 * 
	 * @param productIdentifier 
	 */
	public PersistentOrderLine(ProductIdentifier productIdentifier)
	{
		this(productIdentifier, Iterables.<ProductFeature>empty(), 1);
	}

	/**
	 * 
	 * @param productIdentifier
	 * @param numberOrdered
	 */
	public PersistentOrderLine(ProductIdentifier productIdentifier, int numberOrdered)
	{
		this(productIdentifier, Iterables.<ProductFeature>empty(), numberOrdered);
	}

	/**
	 * 
	 * @param productIdentifier
	 * @param productFeatures
	 */
	public PersistentOrderLine(ProductIdentifier productIdentifier, Iterable<ProductFeature> productFeatures)
	{
		this(productIdentifier, productFeatures, 1);
	}

	/**
	 * 
	 * @param productIdentifier
	 * @param productFeatures
	 * @param numberOrdered
	 */
	public PersistentOrderLine(ProductIdentifier productIdentifier, Iterable<ProductFeature> productFeatures, int numberOrdered)
	{

		Objects.requireNonNull(productFeatures, "productFeatures");
		Product product = Database.INSTANCE.getEntityManagerFactory().createEntityManager().find(PersistentProduct.class, productIdentifier);
		if (product == null)
		{
			throw new IllegalStateException("Product " + productIdentifier + " is unknown/not in catalog");
		}
		this.productIdentifier = product.getIdentifier();
		this.productName = product.getName();
		this.productFeatures = Iterables.asSet(productFeatures);

		if (numberOrdered <= 0)
		{
			throw new IllegalArgumentException("numberOrdered must be greater than 0");
		}
		this.numberOrdered = numberOrdered;
		
		Money price = product.getPrice();
		for (ProductFeature pf : this.productFeatures)
		{
			price = price.add(pf.getPrice());
		}
		// TODO @Hannes need something like this:
		// this.price = price.multiply(numberOrdered)
		// OR
		// Quantity quantity = new Quantity(numberOrdered, Metric.PIECES, RoundingStrategy.ROUND_ONE);
		// this.price = price.multiply(quantity);
		// but have to use this, kind of hacky
		for(int n = 0; n < numberOrdered; n++) {
			this.price = this.price.add(price);
		}
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

	/**
	 * Convenience method
	 * @return the productname of the {@link ProductInstance} in this orderline
	 */
	public final String getProductName()
	{
		return productName;
	}

	@Override
	public final int getNumberOrdered()
	{
		return numberOrdered;
	}

	@Override
	public final Iterable<ProductFeature> getProductFeatures()
	{
		return Iterables.of(productFeatures);
	}
	
	@Override
	public boolean equals(Object other)
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
	public int hashCode() {
		return orderLineIdentifier.hashCode();
	}
	
	@Override
	public String toString() {
		return productIdentifier.toString() + " x " + numberOrdered;
	}
}
