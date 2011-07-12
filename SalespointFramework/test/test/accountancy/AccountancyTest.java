package test.accountancy;

import static org.junit.Assert.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.RollbackException;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.accountancy.Accountancy;
import org.salespointframework.core.accountancy.AccountancyEntry;
import org.salespointframework.core.accountancy.ProductPaymentEntry;
import org.salespointframework.core.accountancy.SomeOtherEntry;
import org.salespointframework.core.accountancy.payment.Cash;
import org.salespointframework.core.accountancy.payment.OrderPayment;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.order.actions.PaymentAction;
import org.salespointframework.util.ArgumentNullException;

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
		System.out.println("Creating AccountancyEntries: ");
		for (int year = 2000; year < 2010; year++) {
			if(year % 2 == 0) {
				PaymentAction pa = new PaymentAction(new OrderPayment(new Cash(), new DateTime(), "me", "you" ));
				a.addEntry(pa.getProductPaymentEntry());
			} else {
				a.addEntry(new SomeOtherEntry());
			}
				
			if(year == 2002)
				from = new DateTime();
			if(year == 2008)
				to = new DateTime();
			try {
				Thread.sleep(1*1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		System.out.println("Done.");
	}

	@Test
	public void select() {
		Iterable<AccountancyEntry> i = a.getEntries(from, to);
		
		//TODO not really a test, because the Iterable is always non-null.
		//Instead, we need to test for non-emptyness of the Iterable, or three
		//elements.
		assertNotNull(i);
		System.out.println("Entries from " + from + " to " + to + ":");
		for(AccountancyEntry e : i) {
			System.out.println(e.toString());
		}
	}
	
	@Test
	public void selectType() {
		a.addEntry(new SomeOtherEntry());
		System.out.println("AccountancyEntries: ");
		Iterable<ProductPaymentEntry> i = a.getEntries(ProductPaymentEntry.class, from, to);
		for(AccountancyEntry e : i) {
			System.out.println(e.toString());
		}
		System.out.println("SomeOtherEntry:");
		Iterable<SomeOtherEntry> f = a.getEntries(SomeOtherEntry.class, from, to);
		for(SomeOtherEntry e : f) {
			System.out.println(e.toString());
		}
		
	}
	
	//@Test(expected=RollbackException.class)
	public void doubleAdd() {
		Accountancy a = new Accountancy();
		PaymentAction pa = new PaymentAction(new OrderPayment(Cash.CASH, new DateTime(), "me", "you" ));
		AccountancyEntry e = new ProductPaymentEntry(pa);
		a.addEntry(e);
		System.out.println(new DateTime());
		try {
			Thread.sleep(1*1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		pa = new PaymentAction(new OrderPayment(Cash.CASH, new DateTime(), "me", "you" ));
		a.addEntry(new ProductPaymentEntry(pa));
		System.out.println(new DateTime());
	}

}
