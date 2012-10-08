package org.salespointframework.core.product;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


import org.salespointframework.core.money.Money;
import org.salespointframework.core.quantity.Metric;
import org.salespointframework.util.Iterables;

public class TransientProduct implements Product, Comparable<TransientProduct> {

	private ProductIdentifier productIdentifier = new ProductIdentifier();

	private String name;
	private Money price;
	private Metric metric;

	protected Set<String> categories = new HashSet<String>();
	
	
	public TransientProduct(String name, Money price, Metric metric)
	{
		this.name = Objects.requireNonNull(name, "name must not be null");
		this.price = Objects.requireNonNull(price, "price must not be null");
		this.metric = Objects.requireNonNull(metric, "metric must not be null");
	}
	
	
	@Override
	public ProductIdentifier getIdentifier() {
		return productIdentifier;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Money getPrice() {
		return price;
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
