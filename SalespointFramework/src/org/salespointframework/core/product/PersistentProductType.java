package org.salespointframework.core.product;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.salespointframework.core.money.Money;
import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;

/**
 * TODO
 * @author Paul Henke
 * 
 */
@Entity
public class PersistentProductType implements ProductType, Comparable<PersistentProductType>
{
	@EmbeddedId
	private ProductIdentifier productIdentifier = new ProductIdentifier();

	private String name;
	private Money price;

	@ElementCollection
	private Set<ProductFeature> productFeatures = new HashSet<ProductFeature>();

	@ElementCollection
	private Set<String> categories = new HashSet<String>();

	/**
	 * Parameterless constructor required for JPA. Do not use.
	 */
	@Deprecated
	protected PersistentProductType()
	{
	}

	/**
	 * Creates a new PersistentProductType
	 * @param name the name of the PersistentProductType
	 * @param price the price of the PersistentProductType
	 */
	public PersistentProductType(String name, Money price)
	{
		this.name = Objects.requireNonNull(name, "name");
		this.price = Objects.requireNonNull(price, "price");
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
		if (other instanceof PersistentProductType)
		{
			return this.productIdentifier.equals(((PersistentProductType)other).productIdentifier);
		}
		return false;
	}

	@Override
	public final int hashCode()
	{
		return productIdentifier.hashCode();
	}
	
	@Override
	public String toString()
	{
		return name;
	}

	@Override
	public final String getName()
	{
		return name;
	}

	@Override
	public Money getPrice()
	{
		return price;
	}

	@Override
	public final Iterable<ProductFeature> getProductFeatures()
	{
		return Iterables.from(productFeatures);
	}

	@Override
	public final ProductIdentifier getIdentifier()
	{
		return productIdentifier;
	}

	@Override
	public final boolean addProductFeature(ProductFeature productFeature)
	{
		Objects.requireNonNull(productFeature, "productFeature");
		return productFeatures.add(productFeature);
	}

	@Override
	public final boolean removeProductFeature(ProductFeature productFeature)
	{
		Objects.requireNonNull(productFeature, "productFeature");
		return productFeatures.remove(productFeature);
	}

	@Override
	public final boolean addCategory(String category)
	{
		Objects.requireNonNull(category, "category");
		return categories.add(category);
	}

	@Override
	public final boolean removeCategory(String category)
	{
		Objects.requireNonNull(category, "category");
		return categories.remove(category);
	}

	@Override
	public final Iterable<String> getCategories()
	{
		return Iterables.from(categories);
	}

	@Override
	public int compareTo(PersistentProductType other)
	{
		return this.name.compareTo(other.name);
	}
}
