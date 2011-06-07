package org.salespointframework.core.catalogs;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.salespointframework.core.products.ProductType;

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
	
	// TODO cast verhindern
	@Override
	public Iterable<String> getCategories() {
		return categories;
	}


	@Override
	public Iterable<T> getProductTypes() {
		// TODO Auto-generated method stub
		return null;
	}
}
