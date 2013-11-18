package org.salespointframework.core.rounding;

import java.math.BigDecimal;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;


@SuppressWarnings("javadoc")
public class IsEqual extends TypeSafeMatcher<BigDecimal> {
	private BigDecimal right;
	
	public  IsEqual(BigDecimal right) {
		this.right = right;
	}
	@Override
	public void describeTo(Description description) {
		description.appendText("equal to " + right);
	}

	@Override
	public boolean matchesSafely(BigDecimal number) {
		return number.compareTo(right) == 0;
	}
	
	@Factory
	public static <T> Matcher<BigDecimal> equal(BigDecimal right) {
		return new IsEqual(right);
	}

}
