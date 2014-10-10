package org.salespointframework.quantity;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit tests for {@link Quantity}.
 *
 * @author Oliver Gierke
 * @author Paul Henke
 */
public class QuantityUnitTests {

	/**
	 * @see #9
	 */
	@Test
	public void defaultsToUnitAsMetric() {
		assertThat(Quantity.of(1).getMetric(), is(Metric.UNIT));
	}

	/**
	 * @see #9
	 */
	@Test(expected = IllegalArgumentException.class)
	public void rejectsNullMetric() {
		Quantity.of(0, null);
	}

	/**
	 * @see #9
	 */
	@Test
	public void rejectsIncompatibleMetric() {

		assertThat(Quantity.of(1).isCompatibleWith(Metric.UNIT), is(true));
		assertThat(Quantity.of(1).isCompatibleWith(Metric.KILOGRAM), is(false));
	}

	/**
	 * @see #9
	 */
	@Test(expected = IllegalArgumentException.class)
	public void rejectsIncompatibleQuantityOnAddition() {
		Quantity.of(1).add(Quantity.of(1, Metric.KILOGRAM));
	}

	/**
	 * @see #9
	 */
	@Test
	public void addsQuantitiesCorrectly() {

		assertThat(Quantity.of(1).add(Quantity.of(1)), is(Quantity.of(2)));
		assertThat(Quantity.of(1.5).add(Quantity.of(1.5)), is(Quantity.of(3.0)));
	}

	/**
	 * @see #9
	 */
	@Test(expected = IllegalArgumentException.class)
	public void rejectsIncompatibleQuantityOnSubtraction() {
		Quantity.of(1).subtract(Quantity.of(1, Metric.KILOGRAM));
	}

	/**
	 * @see #64, #65, #9
	 */
	@Test
	public void subtractsQuantitiesCorrectly() {

		assertThat(Quantity.of(10).subtract(Quantity.of(1)), is(Quantity.of(9)));
		assertThat(Quantity.of(10.5).subtract(Quantity.of(7.25)), is(Quantity.of(3.25)));
	}

	/**
	 * @see #34, #9
	 */
	@Test
	public void comparesQuantitiesCorrectly() {

		Quantity five = Quantity.of(5);
		Quantity ten = Quantity.of(10);

		assertThat(five.isLessThan(ten), is(true));
		assertThat(five.isGreaterThan(ten), is(false));

		assertThat(ten.isGreaterThan(five), is(true));
		assertThat(ten.isLessThan(five), is(false));

		assertThat(ten.isGreaterThanOrEqualTo(ten), is(true));
		assertThat(ten.isGreaterThanOrEqualTo(five), is(true));
	}

	/**
	 * @see #9
	 */
	@Test
	public void discoversNegativeQuantity() {

		assertThat(Quantity.of(-1).isNegative(), is(true));
		assertThat(Quantity.of(0).isNegative(), is(false));
		assertThat(Quantity.of(1).isNegative(), is(false));
	}
}
