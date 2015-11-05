package org.salespointframework.accountancy;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Map;
import java.util.Map.Entry;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.is;
import org.javamoney.moneta.Money;
import org.junit.Assert;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
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

public class AccountancyPeriodTests extends AbstractIntegrationTests {

	@Autowired Accountancy<ProductPaymentEntry> accountancy;
	@Autowired UserAccountManager userAccountManager;

	private LocalDateTime from;
	private LocalDateTime to;

	@Before
	public void testSetup() throws Exception {

		UserAccount account = userAccountManager.save(userAccountManager.create("username", "password"));
		OrderIdentifier orderIdentifier = new Order(account).getIdentifier();

		Money oneEuro = Money.of(1, Currencies.EURO);

		for (int day = 1; day < 20; day++) {
                        
                        for(int hour = 10; hour < 19; hour++) {
                                ProductPaymentEntry p = new ProductPaymentEntry(
                                orderIdentifier, account, oneEuro, "Rechnung Nr.2015-03-" + day + "/" + hour, Cash.CASH);
                                p.setDate(LocalDateTime.of(2015, Month.MARCH, day, hour, 0));
                                accountancy.add(p);
                        }

			if (day == 5)
				from = LocalDateTime.of(2015, Month.MARCH, day, 0, 0);
			if (day == 15)
				to = LocalDateTime.of(2015, Month.MARCH, day, 23, 0);
		}
	}

	@Test
	public void periodSetTest() {
                System.out.println("periodeSetTest");
                
		Map<Interval, Iterable<ProductPaymentEntry>> m = accountancy.find(from, to, Duration.ofDays(1L));
                
                assertNotNull(m);
                assertThat(m.size(), is(11));
                
		for (Entry<Interval, Iterable<ProductPaymentEntry>> e : m.entrySet()) {
                    
                        Iterable<ProductPaymentEntry> oneYearIterable = e.getValue();
                        assertThat(oneYearIterable, Matchers.<ProductPaymentEntry>iterableWithSize(9));
		}
	}

	@Test
	public void singlePeriodTest() {
                System.out.println("singlePeriodTest");
            
		Map<Interval, Iterable<ProductPaymentEntry>> m = accountancy.find(from, to, Duration.between(from, to));
                
                assertNotNull(m);
                assertThat(m.size(), is(1));
                
		for (Entry<Interval, Iterable<ProductPaymentEntry>> e : m.entrySet()) {
                    
			Iterable<ProductPaymentEntry> oneYearIterable = e.getValue();
                        
                        // (Period of from to end date) * (Period from 10 clock until 20 clock)
                        int expectedSize = 11*9;
                        assertThat(oneYearIterable, Matchers.<ProductPaymentEntry>iterableWithSize(expectedSize));
		}
	}

	@Test
	public void periodMoneyTest() {
                System.out.println("periodMoneyTest");
            
		Money total = Currencies.ZERO_EURO;
		Map<Interval, Iterable<ProductPaymentEntry>> m = accountancy.find(from, to, Duration.ofDays(1L));
                
                assertNotNull(m);
                assertThat(m.size(), is(11));
                
		for (Entry<Interval, Iterable<ProductPaymentEntry>> e : m.entrySet()) {
                    
                        Money totalOfDay = Currencies.ZERO_EURO;
			for (ProductPaymentEntry p : e.getValue()) {
                                totalOfDay = totalOfDay.add(p.getValue());
				total = total.add(p.getValue());
			}
                        assertThat(totalOfDay, is(Money.of(BigDecimal.valueOf(9L), Currencies.EURO)));
		}
                
                // (Period of from to end date) * (Period from 10 clock until 20 clock)
                int expectedSize = 11*9;
                assertThat(total, is(Money.of(BigDecimal.valueOf(expectedSize), Currencies.EURO)));
                
                Money totalSales = Currencies.ZERO_EURO;
		Map<Interval, Money> sales = accountancy.salesVolume(from, to, Duration.ofDays(1L));
                
                assertNotNull(m);
                assertThat(sales.size(), is(11));
                
		for (Entry<Interval, Money> e : sales.entrySet()) {
                        totalSales = totalSales.add(e.getValue());
			assertThat(e.getValue(), is(Money.of(BigDecimal.valueOf(9L), Currencies.EURO)));
		}
                
                Assert.assertEquals(total, totalSales);
	}
}
