package test.product;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
import org.junit.Test;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.service.ServiceDeliveryStatus;
import org.salespointframework.core.shop.Shop;
import org.salespointframework.util.ArgumentNullException;

@SuppressWarnings("javadoc")
public class ServiceInstanceTest {

	private DateTime a = Shop.INSTANCE.getTime().getDateTime();
	private TestServiceType service = new TestServiceType("Service", new Money(6));
	private TestServiceType service2 = new TestServiceType("Service2", new Money(8),a.plusHours(2),a.plusDays(7));
	
	private TestServiceInstance t1 = new TestServiceInstance(service, a.plusHours(5), a.plusHours(10));
	private TestServiceInstance t2 = new TestServiceInstance(service, a.plusHours(5), a.plusHours(10));
	private TestServiceInstance t3 = new TestServiceInstance(service2, a.plusHours(5), a.plusHours(10));
	
	@Test(expected=ArgumentNullException.class)
	public void testNotNullName() {
		@SuppressWarnings("unused")
		TestServiceInstance t = new TestServiceInstance(null, a, a);
	}
	
	@Test(expected=ArgumentNullException.class)
	public void testNotNullStart() {
		@SuppressWarnings("unused")
		TestServiceInstance t = new TestServiceInstance(service, null, a.plusHours(4));
	}
	
	@Test(expected=ArgumentNullException.class)
	public void testNotNullEnd() {
		@SuppressWarnings("unused")
		TestServiceInstance t = new TestServiceInstance(service, a.plusHours(4), null);
		}
	
	@Test(expected=IllegalArgumentException.class)
	public void testEndBeforeStart() {
		@SuppressWarnings("unused")
		TestServiceType t = new TestServiceType("Service", Money.ZERO, a.plusHours(8), a.plusHours(3));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testStartBeforePeriodStart() {
		@SuppressWarnings("unused")
		TestServiceType t = new TestServiceType("Service", Money.ZERO, a.minusDays(7), a.plusDays(3));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testEndAfterPeriodEnd() {
		@SuppressWarnings("unused")
		TestServiceType t = new TestServiceType("Service", Money.ZERO, a.plusDays(1), a.minusDays(3));
	}
	
	@Test
	public void testGetStart(){
		
		assertEquals(a.plusHours(5),t1.getStart());
	}
	
	@Test
	public void testGetEnd(){
		
		assertEquals(a.plusHours(10), t1.getEnd());
	}
	
	@Test
	public void testGetServiceType(){
		
		assertEquals(service,t1.getServiceType());
	}

	@Test
	public void testGetServiceDeliveryStatus1(){
		
		assertEquals(t1.getServiceDeliveryStatus(), ServiceDeliveryStatus.SCHEDULED);
	}

	@Test
	public void testGetServiceDeliveryStatus2(){
		
		assertEquals(t1.getServiceDeliveryStatusOnTime(a.plusHours(7)), ServiceDeliveryStatus.EXECUTING);
	}
	
	@Test
	public void testGetServiceDeliveryStatus3(){
		
		assertEquals(t1.getServiceDeliveryStatusOnTime(a.plusHours(20)), ServiceDeliveryStatus.COMPLETED);
	}
	
	@Test
	public void testGetServiceDeliveryStatus4(){
		
		t1.cancelServiceInstance();
		assertEquals(t1.getServiceDeliveryStatus(), ServiceDeliveryStatus.CANCELLED);
	}
	
	@Test
	public void getPrice(){
		
		assertEquals(t1.getPrice(), new Money(6));
	}
	
	@Test
	public void testEquals() {
		TestServiceInstance t = t1;
		
		assertEquals(t,t1);
		
	}
	
	@Test
	public void testNotEquals1() {
		
		assertNotSame(t2, t1);
	}

	@Test
	public void testNotEquals2() {
		
		assertNotSame(t3, t1);
	}
	

	@Test
	public void testEqualsMethod(){
	
		TestServiceInstance t = t1;
		
		assertTrue(t1.equals(t1));
		assertTrue(t1.equals(t));
		assertFalse(t1.equals(t2));
		assertFalse(t1.equals(t3));
	}

	
	@Test 
	public void testHashcode1() {
		
		assertNotSame(t1.hashCode(),t2.hashCode());
	}
	
	@Test 
	public void testHashcode2() {
		
		assertNotSame(t1.hashCode(),t3.hashCode());
	}
}
