package org.salespointframework.core.accountancy;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.core.accountancy.payment.Cash;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.order.OrderIdentifier;
import org.salespointframework.core.useraccount.UserAccount;
import org.salespointframework.core.useraccount.UserAccountIdentifier;
import org.salespointframework.core.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("javadoc")
public class AccountancyTests extends AbstractIntegrationTests {

	@Autowired Accountancy a;
	@Autowired UserAccountManager userAccountManager;

	private DateTime from;
	private DateTime to;

	@Before
	public void testSetup() throws Exception {

		System.out.println("Creating AccountancyEntries: ");

		for (int year = 2000; year < 2010; year++) {

			if ((year % 2) == 0) {
				System.out.println("ProductPaymentEntry");
				UserAccount user = userAccountManager.create(new UserAccountIdentifier(),"");
				user = userAccountManager.save(user);
				a.add(new ProductPaymentEntry(new OrderIdentifier(), user, new Money(1), "Rechnung nr " + year,
						Cash.CASH));
			} else {
				System.out.println("PersistentAccountancyEntry");
				a.add(new AccountancyEntry(new Money(2.22)));
			}

			if (year == 2002) {
				from = new DateTime();
			}
			
			if (year == 2008) {
				to = new DateTime();
			}

			Thread.sleep(1 * 10);

		}
		System.out.println("Done.");
	}

	@Test
	public void select() {

		Iterable<AccountancyEntry> i = a.find(AccountancyEntry.class, from, to);

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
		Iterable<ProductPaymentEntry> i = a.find(ProductPaymentEntry.class, from, to);
		for (AccountancyEntry e : i) {
			System.out.println(e.toString());
		}

		System.out.println("All entries:");
		Iterable<AccountancyEntry> g = a.find(AccountancyEntry.class, from, to);
		for (AccountancyEntry e : g) {
			System.out.println(e.toString());
		}

	}
}
