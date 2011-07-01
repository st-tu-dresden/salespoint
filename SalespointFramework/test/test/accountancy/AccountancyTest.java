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
import org.salespointframework.core.accountancy.ProductPaymentEntry;
import org.salespointframework.core.database.Database;

public class AccountancyTest {
	private Accountancy a;
	
	private DateTime from;
	private DateTime to;
	
	@BeforeClass
	public static void classSetup() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
	}

	@Before
	public void testSetup() {
		a = new Accountancy();
		for (int year = 2000; year < 2005; year++) {
			a.addEntry(new ProductPaymentEntry());
			if(year == 2001)
				from = new DateTime();
			if(year == 2004)
				to = new DateTime();
		}
	}

	@Test
	public void select() {
		Iterable<AccountancyEntry> i = a.getEntries(from, to);
		
		//TODO not really a test, because the Iterable is always non-null.
		//Instead, we need to test for non-emptyness of the Iterable, or three
		//elements.
		assertNotNull(i);

		for(AccountancyEntry e : i) {
			System.out.println(e.toString());
		}
	}
	
	@Test
	public void doubleAdd() {
		Accountancy a = new Accountancy();
		AccountancyEntry e = new ProductPaymentEntry();
		a.addEntry(e);
		System.out.println(new DateTime());
		try {
			Thread.sleep(10*1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		a.addEntry(e);
		System.out.println(new DateTime());
	}

}
