package test.product;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Test;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.shop.Shop;
import org.salespointframework.util.ArgumentNullException;


@SuppressWarnings("javadoc")
public class ServiceTypeTest {

	DateTime a = Shop.INSTANCE.getTime().getDateTime();
	
	TestServiceType t1 = new TestServiceType("Service", new Money(5));
	TestServiceType t2 = new TestServiceType("Service2", new Money(5), a, a.plusHours(5));
	TestServiceType t3 = new TestServiceType("Service3", Money.ZERO, a, a);
	TestServiceType t4 = new TestServiceType("Service", new Money(5));
	
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
	
	@Test(expected=IllegalArgumentException.class)
	public void testEndBeforeStart() {
		@SuppressWarnings("unused")
		TestServiceType t = new TestServiceType("Service", Money.ZERO, a, a.minusDays(3));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testStartBeforeCreationTime() {
		@SuppressWarnings("unused")
		TestServiceType t = new TestServiceType("Service", Money.ZERO, a.minusDays(7), a);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testEndBeforeCreationTime1() {
		@SuppressWarnings("unused")
		TestServiceType t = new TestServiceType("Service", Money.ZERO, a.minusDays(7), a.minusDays(3));
	}
	
	@Test
	public void testGetStartOfPeriod1(){
		
		assertEquals(a, t1.getStartOfPeriodOfOperation());
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
	
	@Test
	public void testEquals() {
		TestServiceType t = t1;
		
		assertEquals(t,t1);
		
	}
	
	@Test
	public void testNotEquals1() {
		
		assertNotSame(t4, t1);
	}

	@Test
	public void testNotEquals2() {
		
		assertNotSame(t3, t1);
	}
	
	@Test
	public void testEqualsMethod(){
	
		TestServiceType t = t1;
		
		assertTrue(t1.equals(t1));
		assertTrue(t1.equals(t));
		assertFalse(t1.equals(t4));
		assertFalse(t1.equals(t3));
	}
	
	@Test 
	public void testHashcode1() {
		
		assertNotSame(t1.hashCode(),t4.hashCode());
	}
	
	@Test 
	public void testHashcode2() {
		
		assertNotSame(t1.hashCode(),t3.hashCode());
	}
}