package org.salespoint.core.data.catalogs;

public interface CatalogEntry {
	String getDescription();
	Iterable<String> getCategories();
}
