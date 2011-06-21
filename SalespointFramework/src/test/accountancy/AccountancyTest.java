package test.accountancy;

import static org.junit.Assert.*;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.accountancy.Accountancy;
import org.salespointframework.core.accountancy.AccountancyEntry;
import org.salespointframework.core.database.Database;

public class AccountancyTest {
	private EntityManagerFactory emf = Database.INSTANCE
			.getEntityManagerFactory();
	private Accountancy a;
	
	private DateTime from;
	private DateTime to;
	
	@BeforeClass
	public static void classSetup() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
	}

	@Before
	public void testSetup() {
		a = new Accountancy(emf.createEntityManager());
		for (int year = 2000; year < 2005; year++) {
			a.addEntry(new AccountancyEntry(String.valueOf(year)));
			if(year == 2001)
				from = new DateTime();
			if(year == 2004)
				to = new DateTime();
		}
	}

	@Test
	public void select() {
		Iterable<AccountancyEntry> i = a.getEntries(from, to);
		
		assertNotNull(i);
		for(AccountancyEntry e : i) {
			System.out.println(e.toString());
		}
	}

}
