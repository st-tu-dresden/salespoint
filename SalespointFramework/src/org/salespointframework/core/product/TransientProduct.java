package org.salespointframework.core.product;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.discount.Discount;
import org.salespointframework.core.product.discount.NoDiscount;
import org.salespointframework.core.quantity.Metric;
import org.salespointframework.core.quantity.Quantity;
import org.salespointframework.util.Iterables;

/**
 * 
 * @author Paul Henke
 * 
 */
public class TransientProduct implements Product, Comparable<TransientProduct> {

	private final ProductIdentifier productIdentifier = new ProductIdentifier();

	private final String name;
	private final Money price;
	private final Metric metric;

	protected Set<String> categories = new HashSet<String>();
	
	private Discount discount = new NoDiscount();
	
	
	public TransientProduct(String name, Money price, Metric metric)
	{
		this(name, price, metric, new NoDiscount());
	}
	
	public TransientProduct(String name, Money price, Metric metric, Discount discount)
	{
		this.name = Objects.requireNonNull(name, "name must not be null");
		this.price = Objects.requireNonNull(price, "price must not be null");
		this.metric = Objects.requireNonNull(metric, "metric must not be null");
		this.discount = Objects.requireNonNull(discount, "discount must not be null");
	}
	
	
	@Override
	public final ProductIdentifier getIdentifier() {
		return productIdentifier;
	}

	@Override
	public final String getName() {
		return name;
	}

	@Override
	public final Money getPrice() {
		return price;
	}
	
	public final Money getDiscountPrice() {
		double factor = discount.getFactor();
		return this.price.multiply(new Money(factor));	//hacky
	}

	@Override
	public boolean addCategory(String category) {
		return categories.add(category);
	}

	@Override
	public boolean removeCategory(String category) {
		return categories.remove(category);
	}

	@Override
	public Iterable<String> getCategories() {
		return Iterables.of(categories);
	}

	@Override
	public Metric getMetric() {
		return metric;
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
		if (other instanceof TransientProduct)
		{
			return this.productIdentifier.equals(((TransientProduct)other).productIdentifier);
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
	public int compareTo(TransientProduct other)
	{
		return this.name.compareTo(other.name);
	}

}
