package test.product;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Test;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.shop.Shop;
import org.salespointframework.util.ArgumentNullException;


public class ServiceTypeTest {

	DateTime a = Shop.INSTANCE.getTime().getDateTime();
	TestServiceType t1 = new TestServiceType("Service", new Money(5));
	TestServiceType t2 = new TestServiceType("Service2", new Money(5), a, a.plusHours(5));

	@Test(expected=ArgumentNullException.class)
	public void testNotNullName() {
		@SuppressWarnings("unused")
		TestServiceType t = new TestServiceType(null, Money.ZERO);
	}
	
	@Test(expected=ArgumentNullException.class)
	public void testNotNullPrice() {
		@SuppressWarnings("unused")
		TestServiceType t = new TestServiceType("Service", null);
	}
	
	@Test(expected=ArgumentNullException.class)
	public void testNotNullStart() {
		@SuppressWarnings("unused")
		TestServiceType t = new TestServiceType("Service", Money.ZERO, null, a);
	}

	@Test(expected=ArgumentNullException.class)
	public void testNotNullEnd() {
		@SuppressWarnings("unused")
		TestServiceType t = new TestServiceType("Service", Money.ZERO, a, null);
	}
	
	@Test
	public void testGetStartOfPeriod1(){
		
		assertTrue(t1.getStartOfPeriodOfOperation().isEqualNow());
	}
	
	@Test
	public void testGetStartOfPeriod2(){
		
		assertEquals(a, t2.getStartOfPeriodOfOperation());
	}
	
	@Test
	public void testGetEndOfPeriod1(){
		
		assertTrue(t2.getEndOfPeriodOfOperation().isAfterNow());
	}
	
	@Test
	public void testGetEndOfPeriod2(){
		
		assertEquals(a.plusHours(5),t2.getEndOfPeriodOfOperation());
	}
}