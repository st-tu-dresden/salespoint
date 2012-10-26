package spielwiese.paul;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.database.Database;


@SuppressWarnings("javadoc")
public class QuantityMoneyClassTest {

	@BeforeClass
	public static void classSetup() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
	}
	
	@Test
	public void newInstance() {
		QuantityMoneyClass qmc = new QuantityMoneyClass();
		System.out.println(qmc.getId());
	}
	
	@Test
	public void newInstance2() {
		QuantityMoneyClass qmc = new QuantityMoneyClass();
		EntityManagerFactory emf = Database.INSTANCE.getEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		
		em.persist(qmc);
		
	}
}
