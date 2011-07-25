package test.order;

import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.order.ChargeLine;
import org.salespointframework.core.order.OrderLine;

public class OrderLineTest {
	
	private EntityManagerFactory emf = Database.INSTANCE
	.getEntityManagerFactory();
	
	
	@BeforeClass
	public static void classSetup() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
		
	}

	@Test
	public void persistOrderLine() {
		
		EntityManager em = emf.createEntityManager();
		long time = new DateTime().getMillis();
		
		OrderLine ol1 = new OrderLine("testdescription1", "testcomment1", 1, new Money(1), new DateTime(time+1));
		OrderLine ol2 = new OrderLine("testdescription2", "testcomment2", 2, new Money(2), new DateTime(time+2));
		
		ChargeLine cl1 = new ChargeLine(new Money(1), "cl1description", "cl1comment");
		ChargeLine cl2 = new ChargeLine(new Money(2), "cl2description", "cl2comment");
		
		ol1.getChargeLines().add(cl1);
		ol2.getChargeLines().add(cl2);
		
		em.getTransaction().begin();
		em.persist(ol1);
		em.persist(ol2);
		em.getTransaction().commit();
		
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
            assertTrue(expectedDeliveryDate.equals(new DateTime(time+1))
                    || expectedDeliveryDate.equals(new DateTime(time+2)));
            
            assertEquals(1, current.getChargeLines().size());
            ChargeLine cl = current.getChargeLines().get(0);
            
            assertTrue(cl.getAmount().getAmount().intValue()==1
                    || cl.getAmount().getAmount().intValue()==2);
            assertTrue(cl.getDescription().equals("cl1description")
                    || cl.getDescription().equals("cl2description"));
            assertTrue(cl.getComment().equals("cl1comment")
                    || cl.getComment().equals("cl2comment"));
        }
	}

}
