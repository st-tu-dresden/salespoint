package test.order;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.order.ChargeLine;
import org.salespointframework.core.order.OrderEntry;
import org.salespointframework.core.order.OrderLine;
import org.salespointframework.core.users.UserIdentifier;

import test.inventory.KeksInventory;
import test.product.KeksInstance;
import test.product.KeksProduct;

public class OrderEntryTest {
	
	private static Logger logger = Logger.getLogger(OrderLineTest.class.getName());
	private EntityManagerFactory emf;
	private EntityManager em;
	
	
	@BeforeClass
	public static void classSetup() {
	}
	
	@AfterClass
	public static void classTearDown() {
	}
	
	@Before 
	public void setUp() {
		
        try {
        	logger.info("Building JPA EntityManager for unit tests");
        	Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
        	emf = Database.INSTANCE.getEntityManagerFactory();
        	em = emf.createEntityManager();
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Exception during JPA EntityManager instanciation.");
        }
	}
	
	@After 
	public void tearDown() {
		
        logger.info("Shuting down JPA layer.");
        if (em != null) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
	}
	
	@Test
	public void testPersistOrderEntry() {
		
        try {

            em.getTransaction().begin();
            
            UserIdentifier ui = new UserIdentifier();
            OrderEntry oe = new OrderEntry(ui, "internet", "testCondition");
            
            em.persist(oe);
            assertTrue(em.contains(oe));

    		ChargeLine cl1 = new ChargeLine(new Money(1), "cl1description", "cl1comment");
    		ChargeLine cl2 = new ChargeLine(new Money(3), "cl2description", "cl2comment");
    		
    		oe.addChargeLine(cl1);
    		oe.addChargeLine(cl2);
    		
    		em.merge(oe);
    		assertTrue(em.contains(oe));

    		
    		final List<ChargeLine> clList = em.createQuery("SELECT c FROM ChargeLine c",
    				ChargeLine.class).getResultList();
    		assertEquals(2, clList.size());
    		
    		for(ChargeLine c : oe.getChargeLines()) {
    			assertTrue(clList.contains(c));
    		}
    		
    		
    		KeksInventory inv = new KeksInventory(em);
    		
    		KeksProduct kp1 = new KeksProduct("bla" ,new Money(1));
    		KeksInstance ki1 = new KeksInstance(kp1);
    		KeksProduct kp2 = new KeksProduct("blub" ,new Money(2));
    		KeksInstance ki2 = new KeksInstance(kp2);
    	
    		inv.addProductInstance(ki1);
    		inv.addProductInstance(ki2);
    		
    		OrderLine ol = new OrderLine(inv, ki1.getSerialNumber(), "testdescription1", "testcomment1");
    		ol.addSerialNumber(ki2.getSerialNumber());
 
    		oe.addOrderLine(ol);
    		em.merge(oe);

    		assertTrue(em.contains(oe));
    		
    		final List<OrderLine> olList = em.createQuery("SELECT o FROM OrderLine o",
    				OrderLine.class).getResultList();
    		assertEquals(1, olList.size());
    		
    		for(OrderLine o : oe.getOrderLines()) {
    			assertTrue(olList.contains(o));
    		}
    		
    		
    		inv.removeProductInstance(ki1.getSerialNumber());
    		inv.removeProductInstance(ki2.getSerialNumber());
    		
    		em.remove(oe);
    		assertFalse(em.contains(oe));
    		assertFalse(em.contains(ol));
    		assertFalse(em.contains(cl1));
    		assertFalse(em.contains(cl2));
    		
            em.getTransaction().commit();

        } catch (Exception ex) {
            em.getTransaction().rollback();
            ex.printStackTrace();
            fail("Exception during testPersistOrderEntry");
        }
		
	}
	
	@Test
	public void testPriceCalculationOrderEntry() {
		
        try {

            
            UserIdentifier ui = new UserIdentifier();
            OrderEntry oe = new OrderEntry(ui, "internet", "testCondition");

    		ChargeLine cl1 = new ChargeLine(new Money(1), "cl1description", "cl1comment");
    		ChargeLine cl2 = new ChargeLine(new Money(3), "cl2description", "cl2comment");
    		ChargeLine cl3 = new ChargeLine(new Money(4), "cl3description", "cl3comment");
    		
    		oe.addChargeLine(cl1);
    		oe.addChargeLine(cl2);
    		
    		KeksInventory inv = new KeksInventory(em);
    		
    		KeksProduct kp1 = new KeksProduct("bla" ,new Money(1));
    		KeksInstance ki1 = new KeksInstance(kp1);
    		KeksProduct kp2 = new KeksProduct("blub" ,new Money(2));
    		KeksInstance ki2 = new KeksInstance(kp2);
    	
    		inv.addProductInstance(ki1);
    		inv.addProductInstance(ki2);
    		
    		OrderLine ol = new OrderLine(inv, ki1.getSerialNumber(), "testdescription1", "testcomment1");
    		ol.addSerialNumber(ki2.getSerialNumber());
    		ol.addChargeLine(cl3);
 
    		oe.addOrderLine(ol);
    		
    		assertTrue(oe.getChargedPrice().equals(new Money(4)));
    		assertTrue(oe.getOrderedObjectsPrice().equals(new Money(7)));
    		assertTrue(oe.getTotalPrice().equals(new Money(11)));
    		
    		inv.removeProductInstance(ki1.getSerialNumber());
    		inv.removeProductInstance(ki2.getSerialNumber());


        } catch (Exception ex) {
            em.getTransaction().rollback();
            ex.printStackTrace();
            fail("Exception during testPriceCalculationOrderEntry");
        }
		
	}

}
