package org.salespointframework.order;

import static org.junit.Assert.*;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.payment.Cash;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;

public class ChargeLineTests extends AbstractIntegrationTests {

	@Autowired UserAccountManager userAccountManager;

	UserAccount user;
	Order order;
	ChargeLine chargeLine;

	@Before
	public void before() {
		user = userAccountManager.create("userId", "password");
		order = new Order(user, Cash.CASH);
		chargeLine = new ChargeLine(Money.zero(CurrencyUnit.EUR), "gaaar nix");
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullTest() {
		order.add((ChargeLine) null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullTest2() {
		order.remove((ChargeLine) null);
	}

	@Test
	public void addTest() {
		assertTrue(order.add(chargeLine));
	}

	@Test
	public void addTest2() {
		order.add(chargeLine);
		assertFalse(order.add(chargeLine));
	}

	@Test
	public void removeTest() {
		order.add(chargeLine);
		assertTrue(order.remove(chargeLine));
	}

	@Test
	public void removeTest2() {
		assertFalse(order.remove(chargeLine));
	}

	@Test
	public void foo() {
		order.add(chargeLine);
		Iterable<ChargeLine> iter = order.getChargeLines();
		for (ChargeLine c : iter) {
			System.out.println(c.getIdentifier());
		}
	}
}
