package org.salespointframework.core.product.features;

import java.util.Set;
import java.util.HashSet;

import java.util.Arrays;

import org.salespointframework.util.SalespointIterable;


public class ProductFeatureType {
	
	private String name;
	private String description;
	
	private Set<ProductFeature> possibleValues;
	
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	
	public boolean addFeature(ProductFeature productFeature) {
		return possibleValues.add(productFeature);
	}
	
	public boolean removeFeature(ProductFeature productFeature) {
		return possibleValues.remove(productFeature);
	}
	
	public ProductFeatureType(String name, String description, ProductFeature... productFeatures) {
		this.name = name;
		this.description = description;
		possibleValues = new HashSet<ProductFeature>(Arrays.asList(productFeatures));
	}
	
	public Iterable<ProductFeature> getPossibleValues() {
		return new SalespointIterable<ProductFeature>(possibleValues);
	}
	
}
