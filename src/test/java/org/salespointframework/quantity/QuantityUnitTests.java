package org.salespointframework.quantity;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.javamoney.moneta.Money;
import org.junit.Test;
import org.salespointframework.core.Currencies;

/**
 * Unit tests for {@link Quantity}.
 *
 * @author Oliver Gierke
 * @author Paul Henke
 */
public class QuantityUnitTests {

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
		Money money = Money.of(2, Currencies.EURO);
		Money result = money.multiply(quantity.amount);

		assertThat(result, is(Money.of(10, Currencies.EURO)));
	}

	@Test
	public void unitTest() {

		Units unit = Units.of(4);
		Money money = Money.of(15.76, Currencies.EURO);
		Money result = money.multiply(unit.amount);

		assertThat(result, is(Money.of(63.04, Currencies.EURO)));
	}

	/**
	 * @see #64, #65
	 */
	@Test
	public void subtractsCorrectly() {

		Quantity q1 = new Quantity(10, Units.METRIC, RoundingStrategy.MONETARY_ROUNDING);
		Quantity q2 = new Quantity(1, Units.METRIC, RoundingStrategy.MONETARY_ROUNDING);

		assertThat(q1.subtract(q2), is(new Quantity(9, Units.METRIC, RoundingStrategy.MONETARY_ROUNDING)));
	}
}
