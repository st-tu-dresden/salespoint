package org.salespointframework.core.product;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


import org.salespointframework.core.money.Money;
import org.salespointframework.core.quantity.Metric;
import org.salespointframework.util.Iterables;

public class TransientProduct implements Product {

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
	public boolean addProductFeature(ProductFeature productFeature) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeProductFeature(
			ProductFeatureIdentifier productFeatureIdentifier) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ProductFeature getProductFeature(
			ProductFeatureIdentifier productFeatureIdentifier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<ProductFeature> getProductFeatures() {
		// TODO Auto-generated method stub
		return null;
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

	public Metric getMetric() {
		return metric;
	}

}
