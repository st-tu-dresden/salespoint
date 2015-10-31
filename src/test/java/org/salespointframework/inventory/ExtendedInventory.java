package org.salespointframework.inventory;

import java.util.List;

import org.salespointframework.quantity.Quantity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Example of an {@link Inventory} extension
 * 
 * @author Oliver Gierke
 */
interface ExtendedInventory extends Inventory<InventoryItem> {

	/**
	 * Returns all {@link InventoryItem}s with {@link Quantity}s of the same metric and an amount greater than the one in
	 * the given {@link Quantity}.
	 * 
	 * @param quantity must not be {@literal null}.
	 * @return
	 */
	@Query("select i from InventoryItem i where i.quantity.amount > ?#{#quantity.amount} and i.quantity.metric = ?#{#quantity.metric}")
	List<InventoryItem> findByQuantityGreaterThan(@Param("quantity") Quantity quantity);
}
