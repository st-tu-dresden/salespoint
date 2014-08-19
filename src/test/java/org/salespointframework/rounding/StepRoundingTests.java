package org.salespointframework.rounding;

import static org.hamcrest.core.AllOf.*;
import static org.hamcrest.core.Is.*;
import static org.junit.Assert.*;
import static org.salespointframework.rounding.IsEqual.*;
import static org.salespointframework.rounding.IsGreaterThan.*;
import static org.salespointframework.rounding.IsGreaterThanOrEqual.*;
import static org.salespointframework.rounding.IsSmallerThan.*;
import static org.salespointframework.rounding.IsSmallerThanOrEqual.*;
import static org.salespointframework.rounding.IsZero.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.quantity.BasicRoundingStrategy;

/**
 * step rounding tests tested is each rounding direction with a value smaller than, equal and greater than the step.
 * Also the value is tested as multiple of step, again smaller than n*step (but greater than (n-1)*step, equal to n*step
 * and greater than n*step (but smaller than (n+1)*step). Also a step value of greater 1 and between 0 and 1 is tested,
 * plus a negative and positive value and a positive and negative step value. This gives 4 (direction) * (3 (size) + 3
 * (size, multiple)) * 2 (fractional step or not) * 2 (sign) * 2 (step sign) = 192 tests. Thus, as single method
 * executes all tests.
 * 
 * @author hannesweisbach
 */
@SuppressWarnings("javadoc")
public class StepRoundingTests {
	static List<RoundingMode> modes = Arrays.asList(RoundingMode.UP, RoundingMode.DOWN, RoundingMode.FLOOR,
			RoundingMode.CEILING);
	static List<BigDecimal> steps = Arrays.asList(BigDecimal.valueOf(50), BigDecimal.valueOf(-50),
			BigDecimal.valueOf(0.005), BigDecimal.valueOf(-0.005));
	static Map<RoundingMode, Map<BigDecimal, Map<BigDecimal, BigDecimal>>> tests = new HashMap<RoundingMode, Map<BigDecimal, Map<BigDecimal, BigDecimal>>>();
	BigDecimal d;

	@BeforeClass
	static public void setup() {
		Map<BigDecimal, Map<BigDecimal, BigDecimal>> test;
		Map<BigDecimal, BigDecimal> result;
		for (RoundingMode mode : modes) {
			test = new HashMap<BigDecimal, Map<BigDecimal, BigDecimal>>();
			/* 2 (fractional) * 2 (step sign) */
			for (BigDecimal step : steps) {
				BigDecimal multipleStep = step.multiply(BigDecimal.valueOf(6));
				/* 2 (sign) * (3 (size) + 3 (size, multiple)) */

				result = new HashMap<BigDecimal, BigDecimal>();
				result.put(step, step);
				result.put(step.negate(), step.negate());
				result.put(multipleStep, multipleStep);
				result.put(multipleStep.negate(), multipleStep.negate());

				/* single, negative */
				result.put(step.multiply(BigDecimal.valueOf(0.95)).negate(), step.negate());
				result.put(step.multiply(BigDecimal.valueOf(1.05)).negate(), step.add(step).negate());

				/* multiple, negative */
				result.put(multipleStep.multiply(BigDecimal.valueOf(0.95)).negate(), multipleStep.negate());
				result.put(multipleStep.multiply(BigDecimal.valueOf(1.05)).negate(), multipleStep.add(step).negate());

				/* single, positive */
				result.put(step.multiply(BigDecimal.valueOf(0.95)), step);
				result.put(step.multiply(BigDecimal.valueOf(1.05)), step.add(step));

				/* multiple, positive */
				result.put(multipleStep.multiply(BigDecimal.valueOf(0.95)), multipleStep);
				result.put(multipleStep.multiply(BigDecimal.valueOf(1.05)), multipleStep.add(step));

				switch (mode) {
					case UP:
						break;
					case CEILING:
						/* single, negative */
						result.put(step.multiply(BigDecimal.valueOf(0.95)).negate(), BigDecimal.ZERO);
						result.put(step.multiply(BigDecimal.valueOf(1.05)).negate(), step.negate());

						/* multiple, negative */
						result.put(multipleStep.multiply(BigDecimal.valueOf(0.95)).negate(), multipleStep.subtract(step).negate());
						result.put(multipleStep.multiply(BigDecimal.valueOf(1.05)).negate(), multipleStep.negate());
						break;
					case DOWN:
						/* single, positive */
						result.put(step.multiply(BigDecimal.valueOf(0.95)), BigDecimal.ZERO);
						result.put(step.multiply(BigDecimal.valueOf(1.05)), step);

						/* single, negative */
						result.put(step.multiply(BigDecimal.valueOf(0.95)).negate(), BigDecimal.ZERO);
						result.put(step.multiply(BigDecimal.valueOf(1.05)).negate(), step.negate());

						/* multiple, positive */
						result.put(multipleStep.multiply(BigDecimal.valueOf(0.95)), multipleStep.subtract(step));
						result.put(step.multiply(BigDecimal.valueOf(6)).multiply(BigDecimal.valueOf(1.05)), multipleStep);

						/* multiple, negative */
						result.put(multipleStep.multiply(BigDecimal.valueOf(0.95)).negate(), multipleStep.subtract(step).negate());
						result.put(multipleStep.multiply(BigDecimal.valueOf(1.05)).negate(), multipleStep.negate());

						break;
					case FLOOR:
						/* single, positive */
						result.put(step.multiply(BigDecimal.valueOf(0.95)), BigDecimal.ZERO);
						result.put(step.multiply(BigDecimal.valueOf(1.05)), step);
						/* multiple, positive */
						result.put(multipleStep.multiply(BigDecimal.valueOf(0.95)), multipleStep.subtract(step));
						result.put(multipleStep.multiply(BigDecimal.valueOf(1.05)), multipleStep);

					default:

				}

				for (Entry<BigDecimal, BigDecimal> e : result.entrySet()) {
					System.out.println(mode + ": " + " for step " + step + " adding <K,V>: <" + e.getKey() + "," + e.getValue()
							+ ">.");
				}

				test.put(step, result);
			}

			tests.put(mode, test);
		}
	}

	private String msg(BigDecimal value, BigDecimal step, RoundingMode mode, BigDecimal result) {
		return "Rounding value " + value + " with step " + step + " in direction " + mode + " with result: " + result;

	}

	@Test
	public void newTest() {
		RoundingMode mode;
		BigDecimal result;
		for (Entry<RoundingMode, Map<BigDecimal, Map<BigDecimal, BigDecimal>>> test : tests.entrySet()) {
			mode = test.getKey();
			for (Entry<BigDecimal, Map<BigDecimal, BigDecimal>> e : test.getValue().entrySet()) {
				BigDecimal step = e.getKey();
				for (Entry<BigDecimal, BigDecimal> pair : e.getValue().entrySet()) {
					result = new BasicRoundingStrategy(step, mode).round(pair.getKey());
					assertThat(msg(pair.getKey(), step, mode, result), result, is(equal(pair.getValue())));
				}
			}

		}
	}

	private void testSmallerSingle(BigDecimal value, BigDecimal step, RoundingMode mode, BigDecimal result) {
		String msg = msg(value, step, mode, result);

		/* between zero and 2*step */
		if (mode == RoundingMode.UP || mode == RoundingMode.CEILING) {
			/* if we are rounding towards positive */
			assertThat(msg, step, is(equal(result)));
		} else {
			assertThat(msg, BigDecimal.ZERO, is(equal(result)));
		}
		if (step.compareTo(BigDecimal.ZERO) < 0) {
			assertThat(msg, result,
					allOf(is(smallerThanOrEqual(BigDecimal.ZERO)), is(greaterThan(step.multiply(BigDecimal.valueOf(2))))));
		} else {
			assertThat(msg, result,
					allOf(is(greaterThanOrEqual(BigDecimal.ZERO)), is(smallerThan(step.multiply(BigDecimal.valueOf(2))))));
		}

	}

	@Test
	public void StepTests() {

		BigDecimal result, remainder;
		String msg;
		for (RoundingMode mode : modes) {
			for (BigDecimal step : steps) {
				BigDecimal smaller = step.subtract(step.divide(BigDecimal.valueOf(2)));
				BigDecimal greater = step.add(step.divide(BigDecimal.valueOf(2)));

				/* smaller than step */
				result = new BasicRoundingStrategy(step, mode).round(smaller);
				remainder = result.remainder(step);

				/* no remainder */
				assertThat(remainder, is(zero()));
				testSmallerSingle(smaller, step, mode, result);

				/* equal step */
				result = new BasicRoundingStrategy(step, mode).round(step);
				remainder = result.remainder(step);
				/* no remainder */
				assertThat("remainder is not zero", remainder, is(zero()));
				assertThat(remainder, is(zero()));
				msg = "Rounding value " + step + " with step " + step + " in direction " + mode + " with result: " + result
						+ " and remainder of division: " + remainder;

				assertThat(msg, step, is(equal(result)));

				/* larger than step */
				result = new BasicRoundingStrategy(step, mode).round(greater);
				remainder = result.remainder(step);
				/* no remainder */
				assertThat("remainder is not zero", remainder, is(zero()));
				assertThat(remainder, is(zero()));
				msg = "Rounding value " + greater + " with step " + step + " in direction " + mode + " with result: " + result
						+ " and remainder of division: " + remainder;

				/* between step an 2*step */
				if (step.compareTo(BigDecimal.ZERO) < 0) {
					assertThat(msg, result,
							allOf(is(smallerThanOrEqual(step)), is(greaterThan(step.multiply(BigDecimal.valueOf(3))))));
				} else {
					assertThat(msg, result,
							allOf(is(greaterThanOrEqual(step)), is(smallerThan(step.multiply(BigDecimal.valueOf(3))))));
				}

			}
		}
	}
}
