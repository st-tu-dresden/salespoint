package test.tran.inventory;

import org.salespointframework.core.inventory.TransientInventoryItem;
import org.salespointframework.core.product.TransientProduct;
import org.salespointframework.core.quantity.Quantity;

public class TransientKeksItem extends TransientInventoryItem
{

	public TransientKeksItem(TransientProduct product, Quantity quantity)
	{
		super(product, quantity);
	}

}
