package test.product;

import static org.junit.Assert.*;

import org.junit.Test;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.quantity.Metric;
import org.salespointframework.core.quantity.Quantity;
import org.salespointframework.core.quantity.rounding.RoundStrategy;
import org.salespointframework.util.ArgumentNullException;


public class MeasuredProductTypeTest {

	Quantity q = new Quantity(300, new Metric("liter", "l"), new RoundStrategy(1,1));
	
	TestMeasuredProductType m1 = new TestMeasuredProductType ("Coke", new Money(600), q);
	TestMeasuredProductType m2 = new TestMeasuredProductType ("Coke", new Money(600), q);
	TestMeasuredProductType m3 = new TestMeasuredProductType ("Fanta", new Money(600), q);
	
	@Test(expected=ArgumentNullException.class)
	public void testNotNullName() {
		@SuppressWarnings("unused")
		TestMeasuredProductType m = new TestMeasuredProductType (null, new Money(2.50), q);
	}
	
	@Test(expected=ArgumentNullException.class)
	public void testNotNullPrice() {
		@SuppressWarnings("unused")
		TestMeasuredProductType m = new TestMeasuredProductType ("Coke", null, q);
	}
	
	@Test(expected=ArgumentNullException.class)
	public void testNotNullQuantity() {
		@SuppressWarnings("unused")
		TestMeasuredProductType m = new TestMeasuredProductType ("Coke", new Money(2.50), null);
	}
	
	@Test
	public void getPrice1() {
		
		assertEquals(new Money(600), m1.getPrice());
	}
	
	@Test
	public void getPrice2() {
		
		Quantity q1 = new Quantity(200,q.getMetric(),q.getRoundingStrategy());
		m1.addQuantity(q1);
		
		assertEquals(new Money(1000), m1.getPrice());
	}
	
	@Test
	public void getPrice3() {
		
		m1.addQuantity(100);
		
		assertEquals(new Money(800), m1.getPrice());
	}
	
	@Test
	public void getPrice5() {
		
		Quantity q1 = new Quantity(200,q.getMetric(),q.getRoundingStrategy());
		m1.reduceQuantityOnHand(q1);
		
		assertEquals(new Money(200), m1.getPrice());
	}
	
	@Test
	public void getPrice6() {
		
		m1.reduceQuantityOnHand(100);
		
		assertEquals(new Money(400), m1.getPrice());
	}
	
	@Test
	public void getUnitPrice() {
		
		assertEquals(new Money(2), m1.getUnitPrice());
	}
	
	@Test
	public void getPreferredMetric() {
		
		assertEquals(q.getMetric(), m1.getPreferredMetric());
	}
	
	@Test
	public void getQuantityOnHand() {
		
		assertEquals(q, m1.getQuantityOnHand());
	}
	
	@Test
	public void reduceQuantity1() {
		
		Quantity q1 = new Quantity(50,q.getMetric(),q.getRoundingStrategy());
		m1.reduceQuantityOnHand(q1);
		
		assertEquals(250, m1.getQuantityOnHand().getAmount().intValue());
	}
	
	@Test
	public void reduceQuantity2() {
		
		Quantity q1 = new Quantity(0,q.getMetric(),q.getRoundingStrategy());
		m1.reduceQuantityOnHand(q1);
		
		assertEquals(300, m1.getQuantityOnHand().getAmount().intValue());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void reduceQuantity3() {
		
		Quantity q1 = new Quantity(-50,q.getMetric(),q.getRoundingStrategy());
		m1.reduceQuantityOnHand(q1);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void reduceQuantity4() {
		
		Quantity q1 = new Quantity(500,q.getMetric(),q.getRoundingStrategy());
		m1.reduceQuantityOnHand(q1);
	}
	
	@Test
	public void reduceQuantity5() {
		
		m1.reduceQuantityOnHand(250);
		
		assertEquals(50, m1.getQuantityOnHand().getAmount().intValue());
	}
	
	@Test
	public void reduceQuantity6() {
		
		m1.reduceQuantityOnHand(0);
		
		assertEquals(300, m1.getQuantityOnHand().getAmount().intValue());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void reduceQuantity7() {
		
		m1.reduceQuantityOnHand(-50);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void reduceQuantity8() {
		
		m1.reduceQuantityOnHand(500);
	}
	
	@Test
	public void addQuantity1() {
		
		m1.addQuantity(250);
		
		assertEquals(550, m1.getQuantityOnHand().getAmount().intValue());
	}
	
	@Test
	public void addQuantity2() {
		
		m1.addQuantity(0);
		
		assertEquals(300, m1.getQuantityOnHand().getAmount().intValue());
	}

	@Test(expected=IllegalArgumentException.class)
	public void addQuantity3() {
		
		m1.addQuantity(-50);
	}
	
	@Test
	public void addQuantity4() {
		
		Quantity q1 = new Quantity(200,q.getMetric(),q.getRoundingStrategy());
		m1.addQuantity(q1);
		
		assertEquals(500, m1.getQuantityOnHand().getAmount().intValue());
	}
	
	@Test
	public void addQuantity5() {
		
		Quantity q1 = new Quantity(0,q.getMetric(),q.getRoundingStrategy());
		m1.addQuantity(q1);
		
		assertEquals(300, m1.getQuantityOnHand().getAmount().intValue());
	}

	@Test(expected=IllegalArgumentException.class)
	public void addQuantity6() {
		
		Quantity q1 = new Quantity(-200,q.getMetric(),q.getRoundingStrategy());
		m1.addQuantity(q1);
	}
	
	@Test
	public void testEquals() {
		TestMeasuredProductType m = m1;
		
		assertEquals(m,m1);
		
	}
	
	@Test
	public void testNotEquals1() {
		
		assertNotSame(m2, m1);
	}

	@Test
	public void testNotEquals2() {
		
		assertNotSame(m3, m1);
	}
	
	@Test
	public void testEqualsMethod(){
	
		TestMeasuredProductType m = m1;
		
		assertTrue(m1.equals(m1));
		assertTrue(m1.equals(m));
		assertFalse(m1.equals(m2));
		assertFalse(m1.equals(m3));
	}
	
	@Test 
	public void testHashcode1() {
		
		assertNotSame(m1.hashCode(),m2.hashCode());
	}
	
	@Test 
	public void testHashcode2() {
		
		assertNotSame(m1.hashCode(),m3.hashCode());
	}
}
