package org.salespointframework.quantity;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.math.RoundingMode;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;

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
		Money money = Money.of(CurrencyUnit.EUR, 2);
		Money result = money.multipliedBy(quantity.amount, RoundingMode.HALF_UP);

		assertThat(result, is(Money.of(CurrencyUnit.EUR, 10)));
	}

	@Test
	public void unitTest() {

		Units unit = Units.of(4);
		Money money = Money.of(CurrencyUnit.EUR, 15.76);
		Money result = money.multipliedBy(unit.amount, RoundingMode.HALF_UP);

		assertThat(result, is(Money.of(CurrencyUnit.EUR, 63.04)));
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
