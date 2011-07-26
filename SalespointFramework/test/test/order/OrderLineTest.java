package test.order;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.order.ChargeLine;
import org.salespointframework.core.order.OrderLine;

public class OrderLineTest {
	
	private EntityManagerFactory emf = Database.INSTANCE
	.getEntityManagerFactory();
	private EntityManager em;
	private long timeStamp;
	
	
	@BeforeClass
	public static void classSetup() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
		
	}
	
	@Before 
	public void setUp() {
		
		em = emf.createEntityManager();
		timeStamp = new DateTime().getMillis();
		
		OrderLine ol1 = new OrderLine("testdescription1", "testcomment1", 1, new Money(1), new DateTime(timeStamp+1));
		OrderLine ol2 = new OrderLine("testdescription2", "testcomment2", 2, new Money(2), new DateTime(timeStamp+2));
		
		ChargeLine cl1 = new ChargeLine(new Money(1), "cl1description", "cl1comment");
		ChargeLine cl2 = new ChargeLine(new Money(2), "cl2description", "cl2comment");
		
		ol1.addChargeLine(cl1);
		ol2.addChargeLine(cl2);
		
		em.getTransaction().begin();
		em.persist(ol1);
		em.persist(ol2);
		em.getTransaction().commit();
	}
	
	@After 
	public void tearDown() {
		
		List<OrderLine> list = em.createQuery("SELECT o FROM OrderLine o",
				OrderLine.class).getResultList();
		
		em.getTransaction().begin();
		for (OrderLine current : list) {
			em.remove(current);
		}
		em.getTransaction().commit();
		em.close();
	}

	@Test
	public void persistOrderLine() {
		
		final List<OrderLine> list = em.createQuery("SELECT o FROM OrderLine o",
				OrderLine.class).getResultList();
		
		assertEquals(2, list.size());
		
		for (OrderLine current : list) {
            final String description = current.getDescription();
            final String comment = current.getComment();
            final int numberOrdered = current.getNumberOrdered();
            final Money unitPrice = current.getUnitPrice();
            final DateTime expectedDeliveryDate = current.getExpectedDeliveryDate();
                    
 
            assertTrue(description.equals("testdescription1")
                    || description.equals("testdescription2"));
            assertTrue(comment.equals("testcomment1")
                    || comment.equals("testcomment2"));
            assertTrue(numberOrdered==1
                    || numberOrdered==2);
            assertTrue(unitPrice.getAmount().intValue()==1
                    || unitPrice.getAmount().intValue()==2);
            assertTrue(expectedDeliveryDate.equals(new DateTime(timeStamp+1))
                    || expectedDeliveryDate.equals(new DateTime(timeStamp+2)));
            
            Iterator<ChargeLine> it = current.getChargeLines().iterator();
            int chargeLineCount = 0;
            
            while(it.hasNext()) {
            	chargeLineCount++;
            	it.next();
            }
            
            assertEquals(1, chargeLineCount);
            
            it = current.getChargeLines().iterator();
            ChargeLine cl = it.next();
            
            assertTrue(cl.getAmount().getAmount().intValue()==1
                    || cl.getAmount().getAmount().intValue()==2);
            assertTrue(cl.getDescription().equals("cl1description")
                    || cl.getDescription().equals("cl2description"));
            assertTrue(cl.getComment().equals("cl1comment")
                    || cl.getComment().equals("cl2comment"));
        }
	}
	
	@Test
	public void calculateAmounts() {
		
		final List<OrderLine> list = em.createQuery("SELECT o FROM OrderLine o",
				OrderLine.class).getResultList();
		
		assertEquals(2, list.size());
		
		Money m1 = new Money(2);
		Money m2 = new Money(6);
		
		for (OrderLine current : list) {
            assertTrue(current.getOrderLinePrice().equals(m1)
                    || current.getOrderLinePrice().equals(m2));
        }
	}

}
