package test.accountancy;

import java.util.Map;
import java.util.Map.Entry;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.accountancy.PersistentAccountancy;
import org.salespointframework.core.accountancy.PersistentProductPaymentEntry;
import org.salespointframework.core.accountancy.payment.Cash;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.order.OrderIdentifier;
import org.salespointframework.core.user.UserIdentifier;

@SuppressWarnings("javadoc")
public class AccountancyPeriodTest {
	private PersistentAccountancy a;

	private DateTime from;
	private DateTime to;

	@BeforeClass
	public static void classSetup() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
	}

	@Before
	public void testSetup() {
		a = new PersistentAccountancy();
		PersistentProductPaymentEntry p;
		System.out.println("Creating AccountancyEntries: ");
		for(int i = 0; i < 20; i++) {
			 p = new PersistentProductPaymentEntry(new OrderIdentifier(), new UserIdentifier(), new Money(1), "Rechnung nr. 3", Cash.CASH);
			 System.out.println("Adding p " + p + " with time " + p.getDate());
			 a.add(p);
			 
			 if(i == 5)
				 from = p.getDate();
			 if(i == 15)
				 to = p.getDate();
			 
			 try {
				 Thread.sleep(100);
			 } catch(InterruptedException e) {
				 e.printStackTrace();
			 }
		}
	}

	@Test
	public void periodSetTest() {
		System.out.println("Getting entries from " + from + " to " + to);
		Map<Interval, Iterable<PersistentProductPaymentEntry>> m = a.find(PersistentProductPaymentEntry.class, from, to, Period.millis(200));
		for(Entry<Interval, Iterable<PersistentProductPaymentEntry>> e : m.entrySet()) {
			System.out.println("ProductPaymentEntries for interval " + e.getKey());
			for(PersistentProductPaymentEntry p : e.getValue()) {
				System.out.println("\t" + p);				
			}
		}
	}

	@Test
	public void singlePeriodTest() {
		System.out.println("Getting entries from " + from + " to " + to);
		Map<Interval, Iterable<PersistentProductPaymentEntry>> m = a.find(PersistentProductPaymentEntry.class, from, to, new Period(from, to));
		for(Entry<Interval, Iterable<PersistentProductPaymentEntry>> e : m.entrySet()) {
			System.out.println("ProductPaymentEntries for interval " + e.getKey());
			for(PersistentProductPaymentEntry p : e.getValue()) {
				System.out.println("\t" + p);
			}
		}
	}
	
	@Test
	public void periodMoneyTest() {
		Money total;
		System.out.println("Getting entries from " + from + " to " + to);
		Map<Interval, Iterable<PersistentProductPaymentEntry>> m = a.find(PersistentProductPaymentEntry.class, from, to, new Period(200));
		for(Entry<Interval, Iterable<PersistentProductPaymentEntry>> e : m.entrySet()) {
			total = Money.ZERO;
			for(PersistentProductPaymentEntry p : e.getValue()) {
				System.out.println("\t" + p.getValue());
				total = total.add(p.getValue());
			}
			System.out.println("Money for interval " + e.getKey() + ": " + total);
			
		}
		System.out.println("Getting entries from " + from + " to " + to);
		Map<Interval, Money> sales = a.salesVolume(PersistentProductPaymentEntry.class, from, to, new Period(200));
		for(Entry<Interval, Money> e : sales.entrySet()) {
			System.out.println("Money for interval " + e.getKey() + ": " + e.getValue());
		}
	}

}
