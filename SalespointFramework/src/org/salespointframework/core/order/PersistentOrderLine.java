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
import org.salespointframework.core.product.PersistentProductType;
import org.salespointframework.core.product.ProductInstance;
import org.salespointframework.core.product.ProductFeature;
import org.salespointframework.core.product.ProductTypeIdentifier;
import org.salespointframework.core.product.ProductType;
import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;

/**
 * TODO
 * @author Paul Henke
 *
 */
@Entity
public class PersistentOrderLine implements OrderLine
{
	@EmbeddedId
	private OrderLineIdentifier orderLineIdentifier = new OrderLineIdentifier();;

	@Embedded
	@AttributeOverride(name = "id", column = @Column(name = "PRODUCT_ID"))
	private ProductTypeIdentifier productTypeIdentifier;

	@ElementCollection
	private Set<ProductFeature> productFeatures = new HashSet<ProductFeature>();

	private int numberOrdered;

	private Money price;

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
	public PersistentOrderLine(ProductTypeIdentifier productIdentifier)
	{
		this(productIdentifier, Iterables.<ProductFeature>empty(), 1);
	}

	/**
	 * 
	 * @param productIdentifier
	 * @param numberOrdered
	 */
	public PersistentOrderLine(ProductTypeIdentifier productIdentifier, int numberOrdered)
	{
		this(productIdentifier, Iterables.<ProductFeature>empty(), numberOrdered);
	}

	/**
	 * 
	 * @param productIdentifier
	 * @param productFeatures
	 */
	public PersistentOrderLine(ProductTypeIdentifier productIdentifier, Iterable<ProductFeature> productFeatures)
	{
		this(productIdentifier, productFeatures, 1);
	}

	/**
	 * 
	 * @param productIdentifier
	 * @param productFeatures
	 * @param numberOrdered
	 */
	public PersistentOrderLine(ProductTypeIdentifier productIdentifier, Iterable<ProductFeature> productFeatures, int numberOrdered)
	{

		Objects.requireNonNull(productFeatures, "productFeatures");
		ProductType productType = Database.INSTANCE.getEntityManagerFactory().createEntityManager().find(PersistentProductType.class, productIdentifier);
		if (productType == null)
		{
			throw new IllegalStateException("ProductType " + productIdentifier + " is unknown/not in catalog");
		}
		this.productTypeIdentifier = productType.getIdentifier();
		this.productName = productType.getName();
		this.productFeatures = Iterables.asSet(productFeatures);

		if (numberOrdered <= 0)
		{
			throw new IllegalArgumentException("numberOrdered must be greater than 0");
		}
		this.numberOrdered = numberOrdered;
		
		Money price = productType.getPrice();
		for (ProductFeature pf : this.productFeatures)
		{
			price = price.add(pf.getPrice());
		}
		this.price = price;
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
	public final ProductTypeIdentifier getProductTypeIdentifier()
	{
		return productTypeIdentifier;
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
		return productTypeIdentifier.toString() + " x " + numberOrdered;
	}
}
