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
import org.salespointframework.core.order.PersistentOrderManager;
import org.salespointframework.core.users.UserIdentifier;

import test.inventory.KeksInventory;
import test.product.KeksInstance;
import test.product.KeksProduct;

public class PersistentOrderManagerTest {
	
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
	public void testOrderManager() {
		
        try {

        	em.getTransaction().begin();
        	
        	PersistentOrderManager pom = new PersistentOrderManager();
            
            UserIdentifier ui = new UserIdentifier();
            OrderEntry oe = new OrderEntry(ui, "internet", "testCondition");
            
            pom.addOrder(oe);
            assertTrue(pom.containsOrderEntry(oe));
            

    		ChargeLine cl1 = new ChargeLine(new Money(1), "cl1description", "cl1comment");
    		ChargeLine cl2 = new ChargeLine(new Money(3), "cl2description", "cl2comment");
    		
    		oe.addChargeLine(cl1);
    		oe.addChargeLine(cl2);
    		
    		pom.update(oe);
    		assertTrue(pom.containsOrderEntry(oe));

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
    		pom.update(oe);
    		assertTrue(pom.containsOrderEntry(oe));
    		
    		final List<OrderLine> olList = em.createQuery("SELECT o FROM OrderLine o",
    				OrderLine.class).getResultList();
    		assertEquals(1, olList.size());
    		
    		for(OrderLine o : oe.getOrderLines()) {
    			assertTrue(olList.contains(o));
    		}
    		
    		
    		inv.removeProductInstance(ki1.getSerialNumber());
    		inv.removeProductInstance(ki2.getSerialNumber());
    		
    		pom.remove(oe.getOrderIdentifier());
    		assertFalse(pom.containsOrderEntry(oe));
    		
            em.getTransaction().commit();

        } catch (Exception ex) {
            em.getTransaction().rollback();
            ex.printStackTrace();
            fail("Exception during testOrderManager");
       }
	}

}
