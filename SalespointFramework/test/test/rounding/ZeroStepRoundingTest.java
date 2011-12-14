package test.rounding;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Test;
import org.salespointframework.core.quantity.rounding.BasicRoundingStrategy;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;
import static test.rounding.IsZero.zero;
import static test.rounding.IsEqual.equal;
import static test.rounding.IsGreaterThan.greaterThan;
import static test.rounding.IsSmallerThan.smallerThan;
import static test.rounding.IsGreaterThanOrEqual.greaterThanOrEqual;
import static test.rounding.IsSmallerThanOrEqual.smallerThanOrEqual;

public class ZeroStepRoundingTest {
	BigDecimal d;

	/* smaller values */
	
	@Test
	public void RoundUpNegL() {
		d = BigDecimal.valueOf(-4.45);
		assertThat(BigDecimal.valueOf(-5), is(equal(new BasicRoundingStrategy(0,
				RoundingMode.UP).round(d))));
	}

	@Test
	public void RoundUpNegE() {
		d = BigDecimal.valueOf(-10);
		assertThat(BigDecimal.valueOf(-10), is(equal(new BasicRoundingStrategy(0,
				RoundingMode.UP).round(d))));
	}

	@Test
	public void RoundUpNegH() {
		d = BigDecimal.valueOf(-11.7);
		assertThat(BigDecimal.valueOf(-12), is(equal(new BasicRoundingStrategy(0,
				RoundingMode.UP).round(d))));
	}

	@Test
	public void RoundUpPosL() {
		d = BigDecimal.valueOf(4.45);
		assertThat(BigDecimal.valueOf(5), is(equal(new BasicRoundingStrategy(0,
				RoundingMode.UP).round(d))));
	}

	@Test
	public void RoundUpPosE() {
		d = BigDecimal.TEN;
		assertThat(BigDecimal.TEN, is(equal(new BasicRoundingStrategy(0,
				RoundingMode.UP).round(d))));
	}

	@Test
	public void RoundUpPosH() {
		d = BigDecimal.valueOf(11.76);
		assertThat(BigDecimal.valueOf(12), is(equal(new BasicRoundingStrategy(0,
				RoundingMode.UP).round(d))));
	}

	@Test
	public void RoundDownNegL() {
		d = BigDecimal.valueOf(-4.45);
		assertThat(BigDecimal.valueOf(-4), is(equal(new BasicRoundingStrategy(0,
				RoundingMode.DOWN).round(d))));
	}

	@Test
	public void RoundDownNegE() {
		d = BigDecimal.TEN.negate();
		assertThat(BigDecimal.TEN.negate(), is(equal(new BasicRoundingStrategy(0,
				RoundingMode.DOWN).round(d))));
	}

	@Test
	public void RoundDownNegH() {
		d = BigDecimal.valueOf(-11.5);
		assertThat(BigDecimal.valueOf(-11), is(equal(new BasicRoundingStrategy(0,
				RoundingMode.DOWN).round(d))));
	}

	@Test
	public void RoundDownPosL() {
		d = BigDecimal.valueOf(4.45);
		assertThat(BigDecimal.valueOf(4), is(equal(new BasicRoundingStrategy(0,
				RoundingMode.DOWN).round(d))));
	}

	@Test
	public void RoundDownPosE() {
		d = BigDecimal.TEN;
		assertThat(BigDecimal.TEN, is(equal(new BasicRoundingStrategy(0,
				RoundingMode.DOWN).round(d))));
	}

	@Test
	public void RoundDownPosH() {
		d = BigDecimal.valueOf(11.56);
		assertThat(BigDecimal.valueOf(11), is(equal(new BasicRoundingStrategy(0,
				RoundingMode.DOWN).round(d))));
	}

	@Test
	public void RoundCeilPosL() {
		d = BigDecimal.valueOf(4.45);
		assertThat(BigDecimal.valueOf(5), is(equal(new BasicRoundingStrategy(0,
				RoundingMode.CEILING).round(d))));
	}

	@Test
	public void RoundCeilPosE() {
		d = BigDecimal.TEN;
		assertThat(BigDecimal.TEN, is(equal(new BasicRoundingStrategy(0,
				RoundingMode.CEILING).round(d))));
	}

	@Test
	public void RoundCeilPosH() {
		d = BigDecimal.valueOf(14.55);
		assertThat(BigDecimal.valueOf(15), is(equal(new BasicRoundingStrategy(0,
				RoundingMode.CEILING).round(d))));
	}

	@Test
	public void RoundCeilNegL() {
		d = BigDecimal.valueOf(-4.45);
		assertThat(BigDecimal.valueOf(-4), is(equal(new BasicRoundingStrategy(0,
				RoundingMode.CEILING).round(d))));
	}

	@Test
	public void RoundCeilNegE() {
		d = BigDecimal.TEN.negate();
		assertThat(BigDecimal.TEN.negate(), is(equal(new BasicRoundingStrategy(0,
				RoundingMode.CEILING).round(d))));
	}

	@Test
	public void RoundCeilNegH() {
		d = BigDecimal.valueOf(-4.55);
		assertThat(BigDecimal.valueOf(-4), is(equal(new BasicRoundingStrategy(0,
				RoundingMode.CEILING).round(d))));
	}

	@Test
	public void RoundFloorPosL() {
		d = BigDecimal.valueOf(4.45);
		assertThat(BigDecimal.valueOf(4), is(equal(new BasicRoundingStrategy(0,
				RoundingMode.FLOOR).round(d))));
	}

	@Test
	public void RoundFloorPosE() {
		d = BigDecimal.TEN;
		assertThat(BigDecimal.TEN, is(equal(new BasicRoundingStrategy(0,
				RoundingMode.FLOOR).round(d))));
	}

	@Test
	public void RoundFloorPosH() {
		d = BigDecimal.valueOf(14.55);
		assertThat(BigDecimal.valueOf(14), is(equal(new BasicRoundingStrategy(0,
				RoundingMode.FLOOR).round(d))));
	}

	@Test
	public void RoundFloorNegL() {
		d = BigDecimal.valueOf(-4.45);
		assertThat(BigDecimal.valueOf(-5), is(equal(new BasicRoundingStrategy(0,
				RoundingMode.FLOOR).round(d))));
	}

	@Test
	public void RoundFloorNegE() {
		d = BigDecimal.TEN.negate();
		assertThat(BigDecimal.TEN.negate(), is(equal(new BasicRoundingStrategy(0,
				RoundingMode.FLOOR).round(d))));
	}

	@Test
	public void RoundFloorNegH() {
		d = BigDecimal.valueOf(-14.55);
		assertThat(BigDecimal.valueOf(-15), is(equal(new BasicRoundingStrategy(0,
				RoundingMode.FLOOR).round(d))));
	}

}
