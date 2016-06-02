package org.salespointframework.catalog;

import org.salespointframework.core.SalespointRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository interface for {@link Product}s
 *
 * @author Oliver Gierke
 */
public interface Catalog<T extends Product> extends SalespointRepository<T, ProductIdentifier> {

	/**
	 * Returns all {@link Product}s assigned to the given category.
	 * 
	 * @param category
	 * @return
	 */
	@Query("select p from #{#entityName} p where :category member of p.categories")
	Iterable<T> findByCategory(String category);

	/**
	 * Returns the {@link Product}s with the given name.
	 * 
	 * @param name
	 * @return
	 */
	Iterable<T> findByName(String name);
}
