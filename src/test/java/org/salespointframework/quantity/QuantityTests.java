package org.salespointframework.quantity;

import java.math.RoundingMode;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.quantity.RoundingStrategy;
import org.salespointframework.quantity.Units;

@SuppressWarnings("javadoc")
public class QuantityTests {

	@Test
	public void multiply() {
		Quantity q = new Quantity(5, Units.METRIC, RoundingStrategy.ROUND_ONE);
		Money m = Money.of(CurrencyUnit.EUR, 2);
		Money r = m.multipliedBy(q.amount, RoundingMode.HALF_UP);
		System.out.println("Result: " + r.toString());
	}

	@Test
	public void unitTest() {
		Units u = Units.of(4);
		Money m = Money.of(CurrencyUnit.EUR, 15.76);
		Money r = m.multipliedBy(u.amount, RoundingMode.HALF_UP);
		System.out.println("Result of " + u.toString() + " * " + m.toString() + " = " + r.toString());
	}
}
