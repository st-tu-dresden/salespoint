package org.salespointframework.quantity;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.math.RoundingMode;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;

public class QuantityTests {

	/**
	 * @see #34
	 */
	@Test
	public void comparesQuantitiesCorrectly() {

		Quantity five = new Quantity(5.0, Units.METRIC, RoundingStrategy.MONETARY_ROUNDING);
		Quantity ten = new Quantity(10.0, Units.METRIC, RoundingStrategy.MONETARY_ROUNDING);

		assertThat(five.isLessThan(ten), is(true));
		assertThat(five.isGreaterThan(ten), is(false));

		assertThat(ten.isGreaterThan(five), is(true));
		assertThat(ten.isLessThan(five), is(false));

		assertThat(ten.isGreaterThanOrEqualTo(ten), is(true));
		assertThat(ten.isGreaterThanOrEqualTo(five), is(true));
	}

	@Test
	public void multiply() {

		Quantity quantity = new Quantity(5, Units.METRIC, RoundingStrategy.ROUND_ONE);
		Money money = Money.of(CurrencyUnit.EUR, 2);
		Money result = money.multipliedBy(quantity.amount, RoundingMode.HALF_UP);

		assertThat(result.toString(), is("EUR 10.00"));
	}

	@Test
	public void unitTest() {

		Units unit = Units.of(4);
		Money money = Money.of(CurrencyUnit.EUR, 15.76);
		Money result = money.multipliedBy(unit.amount, RoundingMode.HALF_UP);

		System.out.println("Result of " + unit.toString() + " * " + money.toString() + " = " + result.toString());
	}
}
