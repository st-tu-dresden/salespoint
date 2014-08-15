package org.salespointframework.catalog;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;

import org.joda.money.Money;
import org.salespointframework.core.AbstractEntity;
import org.salespointframework.quantity.Metric;

/**
 * A product.
 * 
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Entity
public class Product extends AbstractEntity<ProductIdentifier> implements Comparable<Product>
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
		return categories;
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
