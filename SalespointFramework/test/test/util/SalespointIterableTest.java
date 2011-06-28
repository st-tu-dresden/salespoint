package test.util;

import org.junit.Test;
import org.salespointframework.util.ArgumentNullException;
import org.salespointframework.util.SalespointIterable;

public class SalespointIterableTest {
	
	@Test(expected=ArgumentNullException.class)
	public void testNullcheckFromIterable() {
		SalespointIterable.from((Iterable<Object>) null);
	}
	
	@Test(expected=ArgumentNullException.class)
	public void testNullcheckFromArray() {
		SalespointIterable.from((Object[]) null);
	}

	// TODO mehr Tests
	
}
