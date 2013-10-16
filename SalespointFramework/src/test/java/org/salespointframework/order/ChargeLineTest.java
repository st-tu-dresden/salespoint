package org.salespointframework.order;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;

import org.junit.Test;
import org.salespointframework.core.accountancy.payment.Cash;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.order.ChargeLine;
import org.salespointframework.core.order.Order;
import org.salespointframework.core.user.User;
import org.salespointframework.core.user.UserIdentifier;

@SuppressWarnings("javadoc")
public class ChargeLineTest {

	private User user;
	private Order order;
	private ChargeLine chargeLine;

	@Before
	public void before() {
		user = new User(new UserIdentifier(), "");
		order = new Order(user.getIdentifier(), Cash.CASH);
		chargeLine = new ChargeLine(Money.ZERO, "gaaar nix");
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
