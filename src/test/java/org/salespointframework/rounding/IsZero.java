package org.salespointframework.rounding;

import java.math.BigDecimal;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;


@SuppressWarnings("javadoc")
public class IsZero extends TypeSafeMatcher<BigDecimal> {

	@Override
	public void describeTo(Description description) {
		description.appendText("not zero.");
	}

	@Override
	public boolean matchesSafely(BigDecimal number) {
		return number.compareTo(BigDecimal.ZERO) == 0;
	}

	@Factory
	public static <T> Matcher<BigDecimal> zero() {
		return new IsZero();
	}

}
