package org.salespointframework.accountancy;

import java.time.LocalDateTime;
import java.time.Month;
import org.hamcrest.Matchers;
import org.javamoney.moneta.Money;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
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

	// tag::init[]
	@Autowired Accountancy<AccountancyEntry> accountancyGeneral; //<1>
	@Autowired Accountancy<ProductPaymentEntry> accountancySpecial; //<2>
	// end::init[]
	@Autowired UserAccountManager userAccountManager;

	private LocalDateTime from;
	private LocalDateTime to;

	@Before
	public void testSetup() {

		for (int year = 2000; year < 2010; year++) {
                    
			if ((year % 2) == 0) {
				UserAccount user = userAccountManager.create("userId" + year, "password");
				user = userAccountManager.save(user);
				OrderIdentifier orderIdentifier = new Order(user).getIdentifier();

                                ProductPaymentEntry productPaymentEntry = new ProductPaymentEntry(
                                    orderIdentifier,
                                    user,
                                    Money.of(1, Currencies.EURO),
                                    "Rechnung Nr. " + year,
                                    Cash.CASH);
                                productPaymentEntry.setDate(LocalDateTime.of(year, Month.SEPTEMBER, 01, 12, 0));
                                
                                accountancySpecial.add(productPaymentEntry);
			} else {
                                AccountancyEntry accountancyEntry = new AccountancyEntry(
                                        Money.of(2.22, Currencies.EURO), 
                                        "Rechnung Nr. " + year);
                                accountancyEntry.setDate(LocalDateTime.of(year, Month.APRIL, 01, 12, 0));
                                
				accountancyGeneral.add(accountancyEntry);
			}

			if (year == 2002) {
				from = LocalDateTime.of(year, Month.JANUARY, 01, 12, 0);
			}

			if (year == 2008) {
				to = LocalDateTime.of(year, Month.DECEMBER, 01, 12, 0);
			}

		}
	}

	@Test
	public void select() {

		Iterable<AccountancyEntry> i = accountancyGeneral.find(from, to);
                
		assertNotNull(i);
                assertThat(i, Matchers.<AccountancyEntry>iterableWithSize(7));
                
                for (AccountancyEntry e : i) {
                        assertTrue(e.hasDate());
                        assertTrue(e.getDate().get().isAfter(from));
                        assertTrue(e.getDate().get().isBefore(to));
		}
	}

	@Test
	public void selectType() {
            
		Iterable<ProductPaymentEntry> i = accountancySpecial.find(from, to);
                
                assertNotNull(i);
                assertThat(i, Matchers.<ProductPaymentEntry>iterableWithSize(4));
                
		for (AccountancyEntry e : i) {
                        assertTrue(e.hasDate());
                        assertTrue(e.getDate().get().isAfter(from));
                        assertTrue(e.getDate().get().isBefore(to));
		}

		Iterable<AccountancyEntry> g = accountancyGeneral.find(from, to);
                
                assertNotNull(i);
                assertThat(g, Matchers.<AccountancyEntry>iterableWithSize(7));
                
		for (AccountancyEntry e : g) {
                        assertTrue(e.hasDate());
                        assertTrue(e.getDate().get().isAfter(from));
                        assertTrue(e.getDate().get().isBefore(to));
		}

	}
}
