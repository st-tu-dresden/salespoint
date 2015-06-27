package org.salespointframework.accountancy;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.core.Currencies;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderIdentifier;
import org.salespointframework.order.ProductPaymentEntry;
import org.salespointframework.payment.Cash;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("javadoc")
public class AccountancyTests extends AbstractIntegrationTests {

	@Autowired Accountancy<AccountancyEntry> a;
	@Autowired Accountancy<ProductPaymentEntry> p;
	@Autowired UserAccountManager userAccountManager;

	private LocalDateTime from;
	private LocalDateTime to;

	@Before
	public void testSetup() throws Exception {

		System.out.println("Creating AccountancyEntries: ");

		for (int year = 2000; year < 2010; year++) {

			if ((year % 2) == 0) {
				System.out.println("ProductPaymentEntry");
				UserAccount user = userAccountManager.create("userId" + year, "password");
				user = userAccountManager.save(user);
				OrderIdentifier orderIdentifier = new Order(user).getIdentifier();

				a.add(new ProductPaymentEntry(orderIdentifier, user, Money.of(1, Currencies.EURO), "Rechnung nr " + year,
						Cash.CASH));
			} else {
				System.out.println("PersistentAccountancyEntry");
				a.add(new AccountancyEntry(Money.of(2.22, Currencies.EURO)));
			}

			if (year == 2002) {
				from = LocalDateTime.now();
			}

			if (year == 2008) {
				to = LocalDateTime.now();
			}

			Thread.sleep(1 * 10);

		}
		System.out.println("Done.");
	}

	@Test
	public void select() {

		Iterable<AccountancyEntry> i = a.find(from, to);

		// TODO not really a test, because the Iterable is always non-null.
		// Instead, we need to test for non-emptyness of the Iterable, or three
		// elements.
		assertNotNull(i);
		System.out.println("Entries from " + from + " to " + to + ":");
		for (AccountancyEntry e : i) {
			System.out.println(e.toString());
		}
	}

	@Test
	public void selectType() {
		System.out.println("AccountancyEntries: ");
		Iterable<ProductPaymentEntry> i = p.find(from, to);
		for (AccountancyEntry e : i) {
			System.out.println(e.toString());
		}

		System.out.println("All entries:");
		Iterable<AccountancyEntry> g = a.find(from, to);
		for (AccountancyEntry e : g) {
			System.out.println(e.toString());
		}

	}
}
