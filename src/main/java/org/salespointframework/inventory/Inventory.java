package org.salespointframework.inventory;

import java.util.Optional;

import org.salespointframework.catalog.Product;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.core.SalespointRepository;
import org.salespointframework.quantity.Quantity;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository interface for {@link InventoryItem}s.
 * 
 * @author Oliver Gierke
 */
// tag::inventory[]
public interface Inventory<T extends InventoryItem> extends SalespointRepository<T, InventoryItemIdentifier> {

	/**
	 * Returns all {@link InventoryItem}s that are out of stock (i.e. the {@link Quantity}'s amount is equal or less than
	 * zero).
	 * 
	 * @return will never be {@literal null}.
	 */
	@Query("select i from InventoryItem i where i.quantity.amount <= 0")
	Iterable<T> findItemsOutOfStock();

	/**
	 * Returns the {@link InventoryItem} for the {@link Product} with the given identifier.
	 * 
	 * @param productIdentifier must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	@Query("select i from InventoryItem i where i.product.productIdentifier = ?1")
	Optional<T> findByProductIdentifier(ProductIdentifier productIdentifier);

	/**
	 * Returns the {@link InventoryItem} for the given {@link Product}.
	 * 
	 * @param product must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	default Optional<T> findByProduct(Product product) {
		return findByProductIdentifier(product.getId());
	}
}
// end::inventory[]
