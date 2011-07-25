package org.salespointframework.core.catalog;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.salespointframework.core.product.ProductType;
import org.salespointframework.util.Objects;
import org.salespointframework.util.SalespointIterable;

// TODO abstract und internal?
// TODO ganze Klasse überdenken
public class CatalogEntryImpl<T extends ProductType> implements CatalogEntry<T> {
	private String description;
	private Set<String> categories;
	
	public CatalogEntryImpl(String description, String... categories) {
		this.description = Objects.requireNonNull(description, "description");
		Objects.requireNonNull(categories, "categories");
		this.categories = new HashSet<String>(Arrays.asList(categories));		
	}
	
	@Override
	public String getDescription() { 
		return description;
	}
	
	@Override
	public Iterable<String> getCategories() {
		return SalespointIterable.from(categories);
	}


	@Override
	public Iterable<T> getProductTypes() {
		// TODO Auto-generated method stub
		return null;
	}
}
