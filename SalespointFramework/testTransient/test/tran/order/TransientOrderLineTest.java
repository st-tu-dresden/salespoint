package test.tran.order;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.catalog.TransientCatalog;
import org.salespointframework.core.inventory.TransientInventory;
import org.salespointframework.core.inventory.TransientInventoryItem;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.order.OrderCompletionResult;
import org.salespointframework.core.order.TransientOrder;
import org.salespointframework.core.order.TransientOrderLine;
import org.salespointframework.core.order.TransientOrderManager;
import org.salespointframework.core.order.OrderCompletionResult.OrderCompletionStatus;
import org.salespointframework.core.quantity.Units;
import org.salespointframework.core.shop.Shop;
import org.salespointframework.core.user.UserIdentifier;
import org.salespointframework.util.Iterables;

import test.tran.inventory.TransientKeksItem;
import test.tran.product.TransientKeks;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class TransientOrderLineTest
{
	TransientKeks keks = new TransientKeks("Schoki", new Money(0), Units.ZERO.getMetric());
	TransientKeksItem keksItem = new TransientKeksItem(keks, Units.of(10));
	
	static TransientInventory inventory = new TransientInventory();
	TransientCatalog catalog = new TransientCatalog();
	TransientOrderManager ordermanager = new TransientOrderManager();
	
	TransientOrder order;
	
	@BeforeClass
	public static void beforeClass() {
		Shop.INSTANCE.initializeTransient();
	}
	
	@Before
	public void before() {
		catalog.remove(keks.getIdentifier());
		System.out.println( inventory.remove(keksItem.getIdentifier()));
		catalog.add(keks);
		inventory.add(keksItem);
		
		/*
		for(int x = 0; x < 10; x++ ) {
			TransientKeks k = new TransientKeks("Keks " + x,  Money.ZERO ,Units.ONE.getMetric());
			TransientKeksItem kk = new TransientKeksItem(k, Units.from(10));
			catalog.add(k);
			inventory.add(kk);
		}
		*/
		
		//System.out.println(keksItem.getIdentifier() == inventory.get(Keksi, inventoryItemIdentifier));
		
		order = new TransientOrder(new UserIdentifier("JS"));
	}
	
	
	@Test
	public void completeOrderTest() {
		
		//List<TransientInventoryItem> list = Iterables.asList((Shop.INSTANCE.getInventory_().find(TransientInventoryItem.class)));
		Iterable<TransientInventoryItem> list = inventory.find(TransientInventoryItem.class);
		
		System.out.println("-----");
		for(TransientInventoryItem f : list) {
			System.out.println(f.toString());
		}
		System.out.println("-----");
		
		TransientOrderLine orderline = new TransientOrderLine(keks, Units.of(5));
		
		order.addOrderLine(orderline);
		
		OrderCompletionResult result = order.completeOrder();
		
		assertEquals(OrderCompletionStatus.SUCCESSFUL, result.getStatus() );
		
	}
	
	@Test
	public void completeOrderTest2() {
		
		TransientOrderLine orderline = new TransientOrderLine(keks, Units.of(15));
		
		order.addOrderLine(orderline);
		
		OrderCompletionResult result = order.completeOrder();
		
		assertEquals(OrderCompletionStatus.FAILED, result.getStatus() );
		
	}
}

