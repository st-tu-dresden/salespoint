package org.salespointframework.core.catalog;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository interface for {@link Product}s
 *
 * @author Oliver Gierke
 */
public interface Products<T extends Product> extends CrudRepository<T, ProductIdentifier> {

	/**
	 * Returns all {@link Product}s assigned to the given category.
	 * 
	 * @param category
	 * @return
	 */
	@Query("select p from #{#entityName} p where ?1 member of p.categories")
	Iterable<T> findByCategory(String category);
	
	/**
	 * Returns the products with the given name.
	 * 
	 * @param name
	 * @return
	 */
	Iterable<T> findByName(String name);
}
