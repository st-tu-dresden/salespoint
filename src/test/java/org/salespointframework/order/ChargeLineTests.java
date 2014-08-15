package org.salespointframework.order;

import static org.junit.Assert.*;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.accountancy.payment.Cash;
import org.salespointframework.order.ChargeLine;
import org.salespointframework.order.Order;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("javadoc")
public class ChargeLineTests  extends AbstractIntegrationTests {

	@Autowired
	private UserAccountManager userAccountManager;
	
	private UserAccount user;
	private Order order;
	private ChargeLine chargeLine;

	@Before
	public void before() {
		user = userAccountManager.create("userId", "");
		order = new Order(user, Cash.CASH);
		chargeLine = new ChargeLine(Money.zero(CurrencyUnit.EUR), "gaaar nix");
	}

	@Test(expected = NullPointerException.class)
	public void nullTest() {
		order.addChargeLine(null);
	}

	@Test(expected = NullPointerException.class)
	public void nullTest2() {
		order.removeChargeLine(null);
	}

	@Test
	public void addTest() {
		assertTrue(order.addChargeLine(chargeLine));
	}

	@Test
	public void addTest2() {
		order.addChargeLine(chargeLine);
		assertFalse(order.addChargeLine(chargeLine));
	}

	@Test
	public void removeTest() {
		order.addChargeLine(chargeLine);
		assertTrue(order.removeChargeLine(chargeLine.getIdentifier()));
	}

	@Test
	public void removeTest2() {
		assertFalse(order.removeChargeLine(chargeLine.getIdentifier()));
	}

	@Test
	public void foo() {
		order.addChargeLine(chargeLine);
		Iterable<ChargeLine> iter = order.getChargeLines();
		for (ChargeLine c : iter) {
			System.out.println(c.getIdentifier());
		}
	}
}
