package test.rounding;

import java.math.BigDecimal;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;

public class IsSmallerThan extends TypeSafeMatcher<BigDecimal> {
	private BigDecimal right;
	
	public  IsSmallerThan(BigDecimal right) {
		this.right = right;
	}
	@Override
	public void describeTo(Description description) {
		description.appendText("not smaller than");
	}

	@Override
	public boolean matchesSafely(BigDecimal number) {
		return number.compareTo(right) < 0;
	}
	
	@Factory
	public static <T> Matcher<BigDecimal> smallerThan(BigDecimal right) {
		return new IsSmallerThan(right);
	}

}
