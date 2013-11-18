	package org.salespointframework.core.accountancy;

import java.util.Map;
import java.util.Map.Entry;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.salespointframework.core.accountancy.Accountancy;
import org.salespointframework.core.accountancy.ProductPaymentEntry;
import org.salespointframework.core.money.Money;
import org.springframework.transaction.annotation.Transactional;

// FIXME
@SuppressWarnings("javadoc")
@Transactional
public class AccountancyPeriodTest { // extends AbstractIntegrationTests {
	//@Autowired 
	Accountancy a;

	private DateTime from;
	private DateTime to;

	//@Before
	public void testSetup() throws Exception {

		// ProductPaymentEntry p;
		System.out.println("Creating AccountancyEntries: ");
		for (int i = 0; i < 20; i++) {
			
			// p = null; //new ProductPaymentEntry(new OrderIdentifier(), new UserAccountIdentifier(), new Money(1),
			// "Rechnung nr. 3", Cash.CASH);
			// System.out.println("Adding p " + p + " with time " + p.getDate());
			// a.add(p);

			// if (i == 5)
			// from = p.getDate();
			// if (i == 15)
			// to = p.getDate();

			Thread.sleep(5);
		}
	}

	//@Test
	public void periodSetTest() {
		System.out.println("Getting entries from " + from + " to " + to);
		Map<Interval, Iterable<ProductPaymentEntry>> m = a.find(ProductPaymentEntry.class, from, to, Period.millis(200));
		for (Entry<Interval, Iterable<ProductPaymentEntry>> e : m.entrySet()) {
			System.out.println("ProductPaymentEntries for interval " + e.getKey());
			for (ProductPaymentEntry p : e.getValue()) {
				System.out.println("\t" + p);
			}
		}
	}

	//@Test
	public void singlePeriodTest() {
		System.out.println("Getting entries from " + from + " to " + to);
		Map<Interval, Iterable<ProductPaymentEntry>> m = a.find(ProductPaymentEntry.class, from, to, new Period(from, to));
		for (Entry<Interval, Iterable<ProductPaymentEntry>> e : m.entrySet()) {
			System.out.println("ProductPaymentEntries for interval " + e.getKey());
			for (ProductPaymentEntry p : e.getValue()) {
				System.out.println("\t" + p);
			}
		}
	}

	//@Test
	public void periodMoneyTest() {
		Money total;
		System.out.println("Getting entries from " + from + " to " + to);
		Map<Interval, Iterable<ProductPaymentEntry>> m = a.find(ProductPaymentEntry.class, from, to, new Period(200));
		for (Entry<Interval, Iterable<ProductPaymentEntry>> e : m.entrySet()) {
			total = Money.ZERO;
			for (ProductPaymentEntry p : e.getValue()) {
				System.out.println("\t" + p.getValue());
				total = total.add(p.getValue());
			}
			System.out.println("Money for interval " + e.getKey() + ": " + total);

		}
		System.out.println("Getting entries from " + from + " to " + to);
		Map<Interval, Money> sales = a.salesVolume(ProductPaymentEntry.class, from, to, new Period(200));
		for (Entry<Interval, Money> e : sales.entrySet()) {
			System.out.println("Money for interval " + e.getKey() + ": " + e.getValue());
		}
	}

}
