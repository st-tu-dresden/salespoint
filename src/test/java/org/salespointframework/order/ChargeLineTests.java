package org.salespointframework.order;

import org.junit.Before;
import org.junit.Test;
import org.salespointframework.AbstractIntegrationTests;
import org.salespointframework.core.Currencies;
import org.salespointframework.payment.Cash;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Integration tests for {@link ChargeLine}. TODO: Improve test cases (assertions, naming).
 */
public class ChargeLineTests extends AbstractIntegrationTests {

	@Autowired UserAccountManager userAccountManager;

	UserAccount user;
	Order order;
	ChargeLine chargeLine;

	@Before
	public void before() {
		user = userAccountManager.create("userId", "password");
		order = new Order(user, Cash.CASH);
		chargeLine = new ChargeLine(Currencies.ZERO_EURO, "gaaar nix");
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
	public void addsChargeLineCorrectly() {
		order.add(chargeLine);
	}

	@Test
	public void addTest2() {
		order.add(chargeLine);
		order.add(chargeLine);
	}

	@Test
	public void removeTest() {
		order.add(chargeLine);
		order.remove(chargeLine);
	}

	@Test
	public void removeTest2() {
		order.remove(chargeLine);
	}
}
