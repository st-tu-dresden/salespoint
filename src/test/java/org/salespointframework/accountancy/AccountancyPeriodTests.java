package org.salespointframework.accountancy;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Map.Entry;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderIdentifier;
import org.salespointframework.order.ProductPaymentEntry;
import org.salespointframework.payment.Cash;
import org.salespointframework.time.Interval;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;

public class AccountancyPeriodTests extends AbstractIntegrationTests {

	@Autowired Accountancy<ProductPaymentEntry> a;
	@Autowired UserAccountManager userAccountManager;

	private LocalDateTime from;
	private LocalDateTime to;

	@Before
	public void testSetup() throws Exception {

		UserAccount account = userAccountManager.save(userAccountManager.create("username", "password"));
		OrderIdentifier orderIdentifier = new Order(account).getIdentifier();

		Money oneEuro = Money.of(CurrencyUnit.EUR, 1d);

		System.out.println("Creating AccountancyEntries: ");

		for (int i = 0; i < 20; i++) {

			ProductPaymentEntry p = new ProductPaymentEntry(orderIdentifier, account, oneEuro, "Rechnung nr. 3", Cash.CASH);
			a.add(p);

			System.out.println("Adding p " + p);

			if (i == 5)
				from = p.getDate();
			if (i == 15)
				to = p.getDate();

			Thread.sleep(5);
		}
	}

	@Test
	public void periodSetTest() {
		System.out.println("Getting entries from " + from + " to " + to);
		Map<Interval, Iterable<ProductPaymentEntry>> m = a.find(from, to, Duration.ofMillis(200));
		for (Entry<Interval, Iterable<ProductPaymentEntry>> e : m.entrySet()) {
			System.out.println("ProductPaymentEntries for interval " + e.getKey());
			for (ProductPaymentEntry p : e.getValue()) {
				System.out.println("\t" + p);
			}
		}
	}

	@Test
	public void singlePeriodTest() {
		System.out.println("Getting entries from " + from + " to " + to);
		Map<Interval, Iterable<ProductPaymentEntry>> m = a.find(from, to, Duration.between(from, to));
		for (Entry<Interval, Iterable<ProductPaymentEntry>> e : m.entrySet()) {
			System.out.println("ProductPaymentEntries for interval " + e.getKey());
			for (ProductPaymentEntry p : e.getValue()) {
				System.out.println("\t" + p);
			}
		}
	}

	@Test
	public void periodMoneyTest() {
		Money total;
		System.out.println("Getting entries from " + from + " to " + to);
		Map<Interval, Iterable<ProductPaymentEntry>> m = a.find(from, to, Duration.ofMillis(200));
		for (Entry<Interval, Iterable<ProductPaymentEntry>> e : m.entrySet()) {
			total = Money.zero(CurrencyUnit.EUR);
			for (ProductPaymentEntry p : e.getValue()) {
				System.out.println("\t" + p.getValue());
				total = total.plus(p.getValue());
			}
			System.out.println("Money for interval " + e.getKey() + ": " + total);

		}
		System.out.println("Getting entries from " + from + " to " + to);
		Map<Interval, Money> sales = a.salesVolume(from, to, Duration.ofMillis(200));
		for (Entry<Interval, Money> e : sales.entrySet()) {
			System.out.println("Money for interval " + e.getKey() + ": " + e.getValue());
		}
	}
}
