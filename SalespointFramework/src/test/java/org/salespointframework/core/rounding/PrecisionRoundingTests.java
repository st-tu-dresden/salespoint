package org.salespointframework.core.rounding;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Test;
import org.salespointframework.core.quantity.rounding.BasicRoundingStrategy;

/**
 * This class contains tests for Rounding. Tested is the precision rounding,
 * where the number of digits after the decimal delimiter is specified. For each
 * rounding direction (UP, DOWN, FLOOR, CEILING), a value smaller, equal and
 * greater than the target value is tested. What a test does is encoded in its
 * name: "Round-direction-sign-value", where direction is one of the for
 * rounding direction, sign is "Neg" or "Pos", depending on if the value to be
 * rounded is positive or negative. Value is "L", "E" or "H". "L" denotes a
 * value, which has a greater precision than required, but the digit rounded on
 * is smaller than 5. "E" already has the precision to which is rounded to - it
 * should not be affected by rounding. "H" has a higher precision than required
 * an its rounding digit is greater than 5.
 * This gives 4 (direction) * 2 (sign) * 3 (value) = 24 test cases.
 * 
 * @author hannesweisbach
 * 
 */
@SuppressWarnings("javadoc")
public class PrecisionRoundingTests {
	BigDecimal d;

	@Test
	public void RoundUpNegL() {
		d = BigDecimal.valueOf(-4.45);
		assertEquals(BigDecimal.valueOf(-4.5), new BasicRoundingStrategy(1,
				RoundingMode.UP).round(d));
	}

	@Test
	public void RoundUpNegE() {
		d = BigDecimal.valueOf(-4.5);
		assertEquals(BigDecimal.valueOf(-4.5), new BasicRoundingStrategy(1,
				RoundingMode.UP).round(d));
	}

	@Test
	public void RoundUpNegH() {
		d = BigDecimal.valueOf(-4.55);
		assertEquals(BigDecimal.valueOf(-4.6), new BasicRoundingStrategy(1,
				RoundingMode.UP).round(d));
	}

	@Test
	public void RoundUpPosL() {
		d = BigDecimal.valueOf(4.45);
		assertEquals(BigDecimal.valueOf(4.5), new BasicRoundingStrategy(1,
				RoundingMode.UP).round(d));
	}

	@Test
	public void RoundUpPosE() {
		d = BigDecimal.valueOf(4.5);
		assertEquals(BigDecimal.valueOf(4.5), new BasicRoundingStrategy(1,
				RoundingMode.UP).round(d));
	}

	@Test
	public void RoundUpPosH() {
		d = BigDecimal.valueOf(4.55);
		assertEquals(BigDecimal.valueOf(4.6), new BasicRoundingStrategy(1,
				RoundingMode.UP).round(d));
	}

	@Test
	public void RoundDownNegL() {
		d = BigDecimal.valueOf(-4.45);
		assertEquals(BigDecimal.valueOf(-4.4), new BasicRoundingStrategy(1,
				RoundingMode.DOWN).round(d));
	}

	@Test
	public void RoundDownNegE() {
		d = BigDecimal.valueOf(-4.4);
		assertEquals(BigDecimal.valueOf(-4.4), new BasicRoundingStrategy(1,
				RoundingMode.DOWN).round(d));
	}

	@Test
	public void RoundDownNegH() {
		d = BigDecimal.valueOf(-4.55);
		assertEquals(BigDecimal.valueOf(-4.5), new BasicRoundingStrategy(1,
				RoundingMode.DOWN).round(d));
	}

	@Test
	public void RoundDownPosL() {
		d = BigDecimal.valueOf(4.45);
		assertEquals(BigDecimal.valueOf(4.4), new BasicRoundingStrategy(1,
				RoundingMode.DOWN).round(d));
	}

	@Test
	public void RoundDownPosE() {
		d = BigDecimal.valueOf(4.4);
		assertEquals(BigDecimal.valueOf(4.4), new BasicRoundingStrategy(1,
				RoundingMode.DOWN).round(d));
	}

	@Test
	public void RoundDownPosH() {
		d = BigDecimal.valueOf(4.55);
		assertEquals(BigDecimal.valueOf(4.5), new BasicRoundingStrategy(1,
				RoundingMode.DOWN).round(d));
	}

	@Test
	public void RoundCeilPosL() {
		d = BigDecimal.valueOf(4.45);
		assertEquals(BigDecimal.valueOf(4.5), new BasicRoundingStrategy(1,
				RoundingMode.CEILING).round(d));
	}

	@Test
	public void RoundCeilPosE() {
		d = BigDecimal.valueOf(4.5);
		assertEquals(BigDecimal.valueOf(4.5), new BasicRoundingStrategy(1,
				RoundingMode.CEILING).round(d));
	}

	@Test
	public void RoundCeilPosH() {
		d = BigDecimal.valueOf(4.55);
		assertEquals(BigDecimal.valueOf(4.6), new BasicRoundingStrategy(1,
				RoundingMode.CEILING).round(d));
	}

	@Test
	public void RoundCeilNegL() {
		d = BigDecimal.valueOf(-4.45);
		assertEquals(BigDecimal.valueOf(-4.4), new BasicRoundingStrategy(1,
				RoundingMode.CEILING).round(d));
	}

	@Test
	public void RoundCeilNegE() {
		d = BigDecimal.valueOf(-4.4);
		assertEquals(BigDecimal.valueOf(-4.4), new BasicRoundingStrategy(1,
				RoundingMode.CEILING).round(d));
	}

	@Test
	public void RoundCeilNegH() {
		d = BigDecimal.valueOf(-4.55);
		assertEquals(BigDecimal.valueOf(-4.5), new BasicRoundingStrategy(1,
				RoundingMode.CEILING).round(d));
	}

	@Test
	public void RoundFloorPosL() {
		d = BigDecimal.valueOf(4.45);
		assertEquals(BigDecimal.valueOf(4.4), new BasicRoundingStrategy(1,
				RoundingMode.FLOOR).round(d));
	}

	@Test
	public void RoundFloorPosE() {
		d = BigDecimal.valueOf(4.4);
		assertEquals(BigDecimal.valueOf(4.4), new BasicRoundingStrategy(1,
				RoundingMode.FLOOR).round(d));
	}

	@Test
	public void RoundFloorPosH() {
		d = BigDecimal.valueOf(4.55);
		assertEquals(BigDecimal.valueOf(4.5), new BasicRoundingStrategy(1,
				RoundingMode.FLOOR).round(d));
	}

	@Test
	public void RoundFloorNegL() {
		d = BigDecimal.valueOf(-4.45);
		assertEquals(BigDecimal.valueOf(-4.5), new BasicRoundingStrategy(1,
				RoundingMode.FLOOR).round(d));
	}

	@Test
	public void RoundFloorNegE() {
		d = BigDecimal.valueOf(-4.4);
		assertEquals(BigDecimal.valueOf(-4.4), new BasicRoundingStrategy(1,
				RoundingMode.FLOOR).round(d));
	}

	@Test
	public void RoundFloorNegH() {
		d = BigDecimal.valueOf(-4.55);
		assertEquals(BigDecimal.valueOf(-4.6), new BasicRoundingStrategy(1,
				RoundingMode.FLOOR).round(d));
	}

}
