package org.salespoint.core.data.catalogs;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CatalogEntry {
	private String description;
	private Set<String> categories;
	
	public CatalogEntry(String description, String... categories) {
		this.description = description;
		this.categories = new HashSet<String>(Arrays.asList(categories));
	}
	
	
	String getDescription() { 
		return description;
	}
	
	// TODO Dau
	Iterable<String> getCategories() {
		return categories;
	}
}
