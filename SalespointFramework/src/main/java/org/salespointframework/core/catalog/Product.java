package org.salespointframework.core.catalog;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Lob;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.quantity.Metric;
import org.salespointframework.util.Iterables;

/**
 * A persistent implementation of the {@link Product} interface.
 * @author Paul Henke
 * 
 */
@Entity
public class Product implements Comparable<Product>
{
	@EmbeddedId
	@AttributeOverride(name = "id", column = @Column(name = "PRODUCT_ID"))
	private ProductIdentifier productIdentifier = new ProductIdentifier();

	private String name;
	@Lob
	private Money price;

	@ElementCollection
	private Set<String> categories = new HashSet<String>();

	private Metric metric;

	/**
	 * Parameterless constructor required for JPA. Do not use.
	 */
	@Deprecated
	protected Product() { }

	/**
	 * Creates a new Product
	 * 
	 * @param name the name of the Product
	 * @param price the price of the Product
	 * @param metric the {@link Metric} of the Product
	 */
	public Product(String name, Money price, Metric metric)
	{
		this.name = Objects.requireNonNull(name, "name must not be null");
		this.price = Objects.requireNonNull(price, "price must not be null");
		this.metric = Objects.requireNonNull(metric, "metric must not be null");
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
		if (other instanceof Product)
		{
			return this.productIdentifier.equals(((Product)other).productIdentifier);
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

	public String getName()
	{
		return name;
	}

	public Money getPrice()
	{
		return price;
	}



	public final ProductIdentifier getIdentifier()
	{
		return productIdentifier;
	}

	public final boolean addCategory(String category)
	{
		Objects.requireNonNull(category, "category must not be null");
		return categories.add(category);
	}

	public final boolean removeCategory(String category)
	{
		Objects.requireNonNull(category, "category must not be null");
		return categories.remove(category);
	}

	public final Iterable<String> getCategories()
	{
		return Iterables.of(categories);
	}

	@Override
	public int compareTo(Product other)
	{
		return this.name.compareTo(other.name);
	}

	public final Metric getMetric()
	{
		return metric;
	}

	public void setName(String name) {
		this.name = Objects.requireNonNull(name, "name must not be null");
		
	}

	public void setPrice(Money price) {
		this.price = Objects.requireNonNull(price, "price must not be null");
	}
}
