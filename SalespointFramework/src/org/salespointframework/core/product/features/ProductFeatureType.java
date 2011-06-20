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
	
	public boolean addFeature(ProductFeature p) {
		return possibleValues.add(p);
	}
	
	public boolean removeFeature(ProductFeature p) {
		return possibleValues.remove(p);
	}
	
	public ProductFeatureType(String name, String description, Set <ProductFeature> p) {
		this.name = name;
		this.description = description;
		this.possibleValues = new HashSet<ProductFeature>(Arrays.asList(p));
	}
	
	public Iterable<ProductFeature> getPossibleValues() {
		return new SalespointIterable<ProductFeature>(possibleValues);
	}
	
}
