package org.salespointframework.core.inventory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.quantity.Quantity;
import org.salespointframework.core.quantity.Units;
import org.salespointframework.util.Iterables;


/**
 * 
 * @author Paul Henke
 * 
 */
public class TransientInventory implements Inventory<TransientInventoryItem>
{
	private static final Map<InventoryItemIdentifier, TransientInventoryItem> itemMap = new HashMap<>(); 
	
	
	@Override
	public void add(TransientInventoryItem inventoryItem) {
		Objects.requireNonNull(inventoryItem,"inventoryItem must not be null");
		
		if(this.contains(inventoryItem.getIdentifier())) {
			Quantity quantity = inventoryItem.getQuantity();
			inventoryItem = itemMap.get(inventoryItem.getIdentifier());
			inventoryItem.increaseQuantiy(quantity);
		}
		itemMap.put(inventoryItem.getIdentifier(), inventoryItem);
	}

	@Override
	public boolean remove(InventoryItemIdentifier inventoryItemIdentifier)
	{
		Objects.requireNonNull(inventoryItemIdentifier, "inventoryItemIdentifier must not be null");
		return itemMap.remove(inventoryItemIdentifier) != null;
	}

	@Override
	public boolean contains(InventoryItemIdentifier inventoryItemIdentifier)
	{
		Objects.requireNonNull(inventoryItemIdentifier, "inventoryItemIdentifier must not be null");
		return itemMap.containsKey(inventoryItemIdentifier);
	}

	@Override
	public <E extends TransientInventoryItem> E get(Class<E> clazz, InventoryItemIdentifier inventoryItemIdentifier)
	{
		Objects.requireNonNull(clazz, "clazz must not be null");
		Objects.requireNonNull(inventoryItemIdentifier, "inventoryItemIdentifier must not be null");
		
		return clazz.cast(itemMap.get(inventoryItemIdentifier));
	}

	@Override
	public <E extends TransientInventoryItem> Iterable<E> find(Class<E> clazz)
	{
		Objects.requireNonNull(clazz, "clazz must not be null");
		
		List<E> temp = new LinkedList<E>();
		for(TransientInventoryItem inventoryItem : itemMap.values()) 
		{
			if(clazz.isAssignableFrom(inventoryItem.getClass())) 
			{ 
				temp.add(clazz.cast(inventoryItem));
			}
		}
		return Iterables.of(temp);
	}

	@Override
	public <E extends TransientInventoryItem> E getByProductIdentifier(Class<E> clazz, ProductIdentifier productIdentifier)
	{
		Objects.requireNonNull(clazz, "clazz must not be null");
		Objects.requireNonNull(productIdentifier, "productIdentifier must not be null");
		
		for(TransientInventoryItem inventoryItem : itemMap.values()) 
		{
			if(clazz.isAssignableFrom(inventoryItem.getClass()) && inventoryItem.getProduct().getIdentifier().equals(productIdentifier)) 
			{
				return clazz.cast(inventoryItem);
			}
		}
		
		return null;
	};
	
	// TODO
	public Quantity getQuantity(ProductIdentifier productIdentifier) {
		TransientInventoryItem item = this.getByProductIdentifier(TransientInventoryItem.class, productIdentifier);
		if(item == null) {
			return Units.ONE;
		} else {
			return item.getQuantity();
		}
	}
}
