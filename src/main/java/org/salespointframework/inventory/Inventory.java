package org.salespointframework.inventory;

import java.util.Optional;

import org.salespointframework.catalog.Product;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.core.SalespointRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository interface for {@link InventoryItem}s.
 * 
 * @author Oliver Gierke
 */
public interface Inventory<T extends InventoryItem> extends SalespointRepository<T, InventoryItemIdentifier> {

	/**
	 * Returns all {@link InventoryItem}s for the {@link Product} with the given identifier.
	 * 
	 * @param productIdentifier must not be {@literal null}.
	 * @return
	 */
	@Query("select i from InventoryItem i where i.product.productIdentifier = ?1")
	Optional<T> findByProductIdentifier(ProductIdentifier productIdentifier);

	/**
	 * Returns all {@link InventoryItem}s for the given {@link Product}.
	 * 
	 * @param product must not be {@literal null}.
	 * @return
	 */
	default Optional<T> findByProduct(Product product) {
		return findByProductIdentifier(product.getIdentifier());
	}
}
