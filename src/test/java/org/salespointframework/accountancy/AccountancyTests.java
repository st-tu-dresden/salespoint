package org.salespointframework.accountancy;

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
import org.salespointframework.time.Interval;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("javadoc")
public class AccountancyTests extends AbstractIntegrationTests {

	@Autowired Accountancy accountancyEntries;
	@Autowired UserAccountManager userAccountManager;

	LocalDateTime from;
	LocalDateTime to;

	@Before
	public void testSetup() throws Exception {

		System.out.println("Creating AccountancyEntries: ");

		for (int year = 2000; year < 2010; year++) {

			if ((year % 2) == 0) {
				System.out.println("ProductPaymentEntry");
				UserAccount user = userAccountManager.create("userId" + year, "password");
				OrderIdentifier orderIdentifier = new Order(user).getIdentifier();

				accountancyEntries.add(new ProductPaymentEntry(orderIdentifier, user, Money.of(1, Currencies.EURO),
						"Rechnung nr " + year, Cash.CASH));
			} else {
				System.out.println("AccountancyEntry");
				accountancyEntries.add(new AccountancyEntry(Money.of(2.22, Currencies.EURO)));
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

		Interval interval = Interval.from(from).to(to);

		System.out.println("Entries from " + from + " to " + to + ":");

		accountancyEntries.find(interval).forEach(System.out::println);
	}

	@Test
	public void selectType() {

		System.out.println("All entries:");
		accountancyEntries.find(Interval.from(from).to(to)).forEach(System.out::println);
	}
}
