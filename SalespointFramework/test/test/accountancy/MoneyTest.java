package test.accountancy;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.accountancy.payment.Cash;
import org.salespointframework.core.accountancy.payment.OrderPayment;
import org.salespointframework.core.database.Database;

public class MoneyTest {
	private EntityManagerFactory emf = Database.INSTANCE
	.getEntityManagerFactory();
	
	
	@BeforeClass
	public static void classSetup() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
		
	}

	@Test
	public void persistMoney() {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(new Money(1));
		em.getTransaction().commit();
	}
	
	@Test
	public void ZeroTest() {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(Money.ZERO);
		em.getTransaction().commit();
		em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(Money.ZERO);
		em.getTransaction().commit();
	}
	
	@Test
	public void CashTest() {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(new OrderPayment(Cash.CASH, new DateTime(), "from", "to"));
		em.getTransaction().commit();
		em = emf.createEntityManager();
		em.getTransaction().begin();
		//c = new Cash();
		em.persist(new OrderPayment(Cash.CASH, new DateTime(), "from", "to"));
		em.getTransaction().commit();
	}
}
