package test.order;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.inventory.Inventory;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.order.ChargeLine;
import org.salespointframework.core.order.OrderLine;
import org.salespointframework.core.product.ProductInstance;
import org.salespointframework.core.product.SerialNumber;
import org.salespointframework.util.Iterables;

import test.inventory.KeksInventory;
import test.product.KeksInstance;
import test.product.KeksProduct;

public class OrderLineTest {
	
	private EntityManagerFactory emf = Database.INSTANCE
	.getEntityManagerFactory();
	private EntityManager em;
	SerialNumber sn1, sn2, sn3;
	
	
	@BeforeClass
	public static void classSetup() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
		
	}
	
	@Before 
	public void setUp() {
		
		em = emf.createEntityManager();
		
		KeksInventory inv = new KeksInventory(em);
		
		KeksProduct kp1 = new KeksProduct("bla" ,new Money(1));
		KeksInstance ki1 = new KeksInstance(kp1);
		KeksProduct kp2 = new KeksProduct("blub" ,new Money(2));
		KeksInstance ki2 = new KeksInstance(kp2);
	
		inv.addProductInstance(ki1);
		inv.addProductInstance(ki2);
		
		OrderLine ol = new OrderLine(inv, ki1.getSerialNumber(), "testdescription1", "testcomment1");
		ol.addSerialNumber(ki2.getSerialNumber());
		
		//TODO FIX it
		//ChargeLine cl1 = new ChargeLine(new Money(1), "cl1description", "cl1comment");
		//ChargeLine cl2 = new ChargeLine(new Money(3), "cl2description", "cl2comment");
		
		//ol.addChargeLine(cl1);
		//ol.addChargeLine(cl2);
		
		em.getTransaction().begin();
		em.persist(ol);
		em.getTransaction().commit();
	}
	
	@After 
	public void tearDown() {
		
		List<OrderLine> list = em.createQuery("SELECT o FROM OrderLine o",
				OrderLine.class).getResultList();
		
		List<ProductInstance> pis = new ArrayList<ProductInstance>();
		
		for (OrderLine current : list) {
			for(ProductInstance pi : current.getInventory().getProductInstances()) {
				pis.add(pi);
			}
		}
		
		em.getTransaction().begin();
		
		for(OrderLine ol : list) {
			em.remove(ol);
		}
		
		for(ProductInstance pi : pis) {
			em.remove(pi);
		}
		
		em.getTransaction().commit();
		em.close();
	}

	@Test
	public void persistOrderLine() {
		
		final List<OrderLine> list = em.createQuery("SELECT o FROM OrderLine o",
				OrderLine.class).getResultList();
		
		assertEquals(1, list.size());
		
		for (OrderLine current : list) {
            final String description = current.getDescription();
            final String comment = current.getComment();
            final int numberOrdered = current.getNumberOrdered();
 
            assertTrue(description.equals("testdescription1"));
            assertTrue(comment.equals("testcomment1"));
            assertTrue(numberOrdered==2);
            
            Iterator<ChargeLine> it = current.getChargeLines().iterator();
            int chargeLineCount = 0;
            
            while(it.hasNext()) {
            	chargeLineCount++;
            	it.next();
            }
            
/*            assertEquals(2, chargeLineCount);
            
            it = current.getChargeLines().iterator();
            ChargeLine cl = it.next();
            
            assertTrue(cl.getAmount().getAmount().intValue()==1
                    || cl.getAmount().getAmount().intValue()==2);
            assertTrue(cl.getDescription().equals("cl1description")
                    || cl.getDescription().equals("cl2description"));
            assertTrue(cl.getComment().equals("cl1comment")
                    || cl.getComment().equals("cl2comment"));*/
        }
	}
	
	@Test
	public void calculateAmounts() {
		
		final List<OrderLine> list = em.createQuery("SELECT o FROM OrderLine o",
				OrderLine.class).getResultList();
		
		assertEquals(1, list.size());
		
		Money m = new Money(3);
		
		for (OrderLine current : list) {
            assertTrue(current.getOrderLinePrice().equals(m));
        }
	}

}
