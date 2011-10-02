package org.salespointframework.core.product;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.salespointframework.core.money.Money;
import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;
import org.salespointframework.util.Tuple;

/**
 * TODO
 * @author Paul Henke
 *
 */
@Entity
public class PersistentProduct implements Product, Comparable<PersistentProduct>
{
	@EmbeddedId
	private SerialNumber serialNumber = new SerialNumber();

	@Embedded
	@AttributeOverride(name = "id", column = @Column(name = "PRODUCT_ID"))
	private ProductIdentifier productIdentifier;

	private String name;
	private Money price;

	@ElementCollection
	private Set<ProductFeature> productFeatures = new HashSet<ProductFeature>();

	/**
	 * Parameterless constructor required for JPA. Do not use.
	 */
	@Deprecated
	protected PersistentProduct()
	{
	}
	
	/**
	 * Creates an new PersistentProduct
	 * @param productType the {@link ProductType} of the PersistentProduct
	 */
	public PersistentProduct(ProductType productType)
	{
		this(productType, Iterables.<Tuple<String, String>> empty());
	}

	/**
	 * Creates a new PersistentProduct with a specified {@link ProductFeature}set
	 * @param productType the {@link ProductType} of the PersistentProduct
	 * @param features an Iterable of Tuples of {@link ProductFeature}s for the PersistentProduct
	 */
	public PersistentProduct(ProductType productType, Iterable<Tuple<String, String>> features)
	{
		Objects.requireNonNull(features, "features");
		this.productIdentifier = Objects.requireNonNull(productType, "productType").getIdentifier();
		this.name = productType.getName();
		this.price = productType.getPrice(); // TODO CLONE?

		// TODO zu hacky? Jop.
		for (Tuple<String, String> feature : features)
		{
			ProductFeature f = ProductFeature.create(feature.getItem1(), feature.getItem2());

			for (ProductFeature realFeature : productType.getProductFeatures())
			{
				if (f.equals(realFeature))
				{
					if (!productFeatures.contains(realFeature))
					{
						productFeatures.add(realFeature);
					} else
					{
						// TODO Exception ?
					}
				}
			}
		}
	}

	@Override
	public final ProductIdentifier getIdentifier()
	{
		return productIdentifier;
	}

	@Override
	public Money getPrice()
	{
		return price;
	}

	@Override
	public final SerialNumber getSerialNumber()
	{
		return serialNumber;
	}

	@Override
	public final Iterable<ProductFeature> getProductFeatures()
	{
		return Iterables.from(productFeatures);
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
		if (other instanceof PersistentProduct)
		{
			return this.serialNumber.equals(((PersistentProduct)other).serialNumber);
		}
		return false;
	}

	@Override
	public final int hashCode()
	{
		return serialNumber.hashCode();
	}

	@Override
	public String toString()
	{
		return name;
	}

	@Override
	public int compareTo(PersistentProduct other)
	{
		return this.name.compareTo(other.name);
	}
}
