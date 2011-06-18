package org.salespointframework.core.catalog;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.salespointframework.core.product.ProductType;
import org.salespointframework.util.SalespointIterable;

public class CatalogEntryImpl<T extends ProductType> implements CatalogEntry<T> {
	private String description;
	private Set<String> categories;
	
	// TODO nullchecks
	public CatalogEntryImpl(String description, String... categories) {
		this.description = description;
		this.categories = new HashSet<String>(Arrays.asList(categories));		
	}
	
	@Override
	public String getDescription() { 
		return description;
	}
	
	@Override
	public Iterable<String> getCategories() {
		return new SalespointIterable<String>(categories);
	}


	@Override
	public Iterable<T> getProductTypes() {
		// TODO Auto-generated method stub
		return null;
	}
}
