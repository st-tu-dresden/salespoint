package org.salespointframework.accountancy;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Map.Entry;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.core.Currencies;
import org.salespointframework.core.Streamable;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderIdentifier;
import org.salespointframework.payment.Cash;
import org.salespointframework.time.Interval;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;

public class AccountancyPeriodTests extends AbstractIntegrationTests {

	@Autowired Accountancy a;
	@Autowired UserAccountManager userAccountManager;

	LocalDateTime from;
	LocalDateTime to;

	@Before
	public void testSetup() throws Exception {

		UserAccount account = userAccountManager.create("username", "password");
		OrderIdentifier orderIdentifier = new Order(account).getId();

		Money oneEuro = Money.of(1, Currencies.EURO);

		System.out.println("Creating AccountancyEntries: ");

		for (int i = 0; i < 20; i++) {

			ProductPaymentEntry p = new ProductPaymentEntry(orderIdentifier, account, oneEuro, "Rechnung nr. 3", Cash.CASH);
			a.add(p);

			System.out.println("Adding p " + p);

			if (i == 5)
				from = p.getDate().get();
			if (i == 15)
				to = p.getDate().get();

			Thread.sleep(5);
		}
	}

	@Test
	public void periodSetTest() {

		System.out.println("Getting entries from " + from + " to " + to);

		Interval interval = Interval.from(from).to(to);

		Map<Interval, Streamable<AccountancyEntry>> m = a.find(interval, Duration.ofMillis(200));
		for (Entry<Interval, Streamable<AccountancyEntry>> e : m.entrySet()) {
			System.out.println("ProductPaymentEntries for interval " + e.getKey());
			for (AccountancyEntry p : e.getValue()) {
				System.out.println("\t" + p);
			}
		}
	}

	@Test
	public void singlePeriodTest() {
		System.out.println("Getting entries from " + from + " to " + to);

		Interval interval = Interval.from(from).to(to);

		Map<Interval, Streamable<AccountancyEntry>> m = a.find(interval, interval.getDuration());
		for (Entry<Interval, Streamable<AccountancyEntry>> e : m.entrySet()) {
			System.out.println("AccountancyEntry for interval " + e.getKey());
			for (AccountancyEntry p : e.getValue()) {
				System.out.println("\t" + p);
			}
		}
	}

	@Test
	public void periodMoneyTest() {
		MonetaryAmount total;
		System.out.println("Getting entries from " + from + " to " + to);

		Interval interval = Interval.from(from).to(to);

		Map<Interval, Streamable<AccountancyEntry>> m = a.find(interval, Duration.ofMillis(200));
		for (Entry<Interval, Streamable<AccountancyEntry>> e : m.entrySet()) {
			total = Currencies.ZERO_EURO;
			for (AccountancyEntry p : e.getValue()) {
				System.out.println("\t" + p.getValue());
				total = total.add(p.getValue());
			}
			System.out.println("MonetaryAmount for interval " + e.getKey() + ": " + total);

		}
		System.out.println("Getting entries from " + from + " to " + to);
		Map<Interval, MonetaryAmount> sales = a.salesVolume(interval, Duration.ofMillis(200));
		for (Entry<Interval, MonetaryAmount> e : sales.entrySet()) {
			System.out.println("MonetaryAmount for interval " + e.getKey() + ": " + e.getValue());
		}
	}
}
