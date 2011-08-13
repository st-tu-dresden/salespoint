package test.order;

import static org.junit.Assert.*;

import java.util.Iterator;
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
import org.salespointframework.core.order.OrderLine;

import test.inventory.KeksInventory;
import test.product.KeksInstance;
import test.product.KeksProduct;

public class OrderLineTest {
	
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
	public void testPersistOrderLine() {
		
        try {

            em.getTransaction().begin();
            
            //setup
    		KeksInventory inv = new KeksInventory(em);
    		
    		KeksProduct kp1 = new KeksProduct("bla" ,new Money(1));
    		KeksInstance ki1 = new KeksInstance(kp1);
    		KeksProduct kp2 = new KeksProduct("blub" ,new Money(2));
    		KeksInstance ki2 = new KeksInstance(kp2);
    	
    		inv.addProductInstance(ki1);
    		inv.addProductInstance(ki2);
    		
    		OrderLine ol = new OrderLine(inv, ki1.getSerialNumber(), "testdescription1", "testcomment1");
    		ol.addSerialNumber(ki2.getSerialNumber());
 
    		
    		em.persist(ol);
    		assertTrue(em.contains(ol));
    		
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
            }
    		
    		inv.removeProductInstance(ki1.getSerialNumber());
    		inv.removeProductInstance(ki2.getSerialNumber());
    		
    		em.remove(ol);
    		assertFalse(em.contains(ol));
    		
            em.getTransaction().commit();

        } catch (Exception ex) {
            em.getTransaction().rollback();
            ex.printStackTrace();
            fail("Exception during testPersistOrderLine");
        }
	}
	
	
	@Test
	public void testPersistChargeLines() {
		
        try {

            em.getTransaction().begin();
            
            //setup
    		KeksInventory inv = new KeksInventory(em);
    		
    		KeksProduct kp1 = new KeksProduct("bla" ,new Money(1));
    		KeksInstance ki1 = new KeksInstance(kp1);
    	
    		inv.addProductInstance(ki1);
    		
    		OrderLine ol = new OrderLine(inv, ki1.getSerialNumber(), "testdescription1", "testcomment1");
    		
    		em.persist(ol);
    		assertTrue(em.contains(ol));
    		

    		ChargeLine cl1 = new ChargeLine(new Money(1), "cl1description", "cl1comment");
    		ChargeLine cl2 = new ChargeLine(new Money(3), "cl2description", "cl2comment");
    		
    		ol.addChargeLine(cl1);
    		ol.addChargeLine(cl2);
    		
    		em.merge(ol);
    		
    		final List<OrderLine> list = em.createQuery("SELECT o FROM OrderLine o",
    				OrderLine.class).getResultList();
    		
    		assertEquals(1, list.size());
    		
    		for (OrderLine current : list) {
    			
                Iterator<ChargeLine> it = current.getChargeLines().iterator();
                int chargeLineCount = 0;
                
                while(it.hasNext()) {
                	chargeLineCount++;
                    ChargeLine cl = it.next();
                    
                    assertTrue(cl.getAmount().getAmount().intValue()==1
                            || cl.getAmount().getAmount().intValue()==3);
                    assertTrue(cl.getDescription().equals("cl1description")
                            || cl.getDescription().equals("cl2description"));
                    assertTrue(cl.getComment().equals("cl1comment")
                            || cl.getComment().equals("cl2comment"));
                }
                assertEquals(2, chargeLineCount);

            }
    		
    		inv.removeProductInstance(ki1.getSerialNumber());
    		em.remove(ol);
    		assertFalse(em.contains(ol));
    		
            em.getTransaction().commit();

        } catch (Exception ex) {
            em.getTransaction().rollback();
            ex.printStackTrace();
            fail("Exception during testPersistChargeLines");
        }
	}
	
	@Test
	public void testCalculateAmounts() {
		
		
        try {
            
            //setup
    		KeksInventory inv = new KeksInventory(em);
    		
    		KeksProduct kp1 = new KeksProduct("bla" ,new Money(1));
    		KeksInstance ki1 = new KeksInstance(kp1);
    		KeksProduct kp2 = new KeksProduct("blub" ,new Money(2));
    		KeksInstance ki2 = new KeksInstance(kp2);
    	
    		inv.addProductInstance(ki1);
    		inv.addProductInstance(ki2);
    		
    		OrderLine ol = new OrderLine(inv, ki1.getSerialNumber(), "testdescription1", "testcomment1");
    		ol.addSerialNumber(ki2.getSerialNumber());
 
    		ChargeLine cl1 = new ChargeLine(new Money(1), "cl1description", "cl1comment");
    		ChargeLine cl2 = new ChargeLine(new Money(3), "cl2description", "cl2comment");
    		
    		ol.addChargeLine(cl1);
    		ol.addChargeLine(cl2);

    		Money m = new Money(7);
    		assertTrue(ol.getOrderLinePrice().equals(m));

    		inv.removeProductInstance(ki1.getSerialNumber());
    		inv.removeProductInstance(ki2.getSerialNumber());

        } catch (Exception ex) {
            em.getTransaction().rollback();
            ex.printStackTrace();
            fail("Exception during testCalculateAmounts");
        }
	}

}
