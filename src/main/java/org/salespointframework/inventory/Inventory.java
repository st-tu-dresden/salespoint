package org.salespointframework.inventory;

import java.util.Optional;

import org.salespointframework.catalog.Product;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.core.SalespointRepository;

/**
 * Repository interface for {@link InventoryItem}s.
 * 
 * @author Oliver Gierke
 */
public interface Inventory<T extends InventoryItem> extends SalespointRepository<T, InventoryItemIdentifier> {

	/**
	 * Returns all {@link InventoryItem}s for the {@link Product} with the given identifier.
	 * 
	 * @param productIdentifier
	 * @return
	 */
	Optional<T> findByProductProductIdentifier(ProductIdentifier productIdentifier);
}
