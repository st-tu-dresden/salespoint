package test.product;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.quantity.Metric;
import org.salespointframework.core.quantity.Quantity;
import org.salespointframework.core.quantity.rounding.RoundStrategy;
import org.salespointframework.util.ArgumentNullException;

public class MeasuredProductInstanceTest {

	Quantity q = new Quantity(300, new Metric("liter", "l"), new RoundStrategy(1, 1));
	
	TestMeasuredProductType coke = new TestMeasuredProductType ("Coke", new Money(600), q);
	
	TestMeasuredProductInstance klein1 = new TestMeasuredProductInstance (coke, new Quantity(0.2, q.getMetric(), q.getRoundingStrategy()));
	TestMeasuredProductInstance klein2 = new TestMeasuredProductInstance (coke, new Quantity(0.2, q.getMetric(), q.getRoundingStrategy()));
	TestMeasuredProductInstance gross = new TestMeasuredProductInstance (coke, new Quantity(0.5, q.getMetric(), q.getRoundingStrategy()));
	
	@Test(expected=ArgumentNullException.class)
	public void testNotNullMeasuredType() {
		@SuppressWarnings("unused")
		TestMeasuredProductInstance m = new TestMeasuredProductInstance (null, new Quantity(0.5, q.getMetric(), q.getRoundingStrategy()));
	}
	
	@Test(expected=ArgumentNullException.class)
	public void testNotNullQuantity() {
		Quantity q = null;
		@SuppressWarnings("unused")
		TestMeasuredProductInstance m = new TestMeasuredProductInstance (coke, q);
	}	
	
	@Test 
	public void testGetProductType(){
		
		assertEquals (coke, klein1.getProductType());
	}
	
	@Test 
	public void testGetPrice(){
		
		assertEquals (new Money(0.4), klein1.getPrice());
	}
	
	@Test 
	public void testGetQuantityOfProductType(){
		
		TestMeasuredProductType coke1 = new TestMeasuredProductType ("Coke", new Money(600), q);
		TestMeasuredProductInstance m = new TestMeasuredProductInstance (coke1, new Quantity(0.5, q.getMetric(), q.getRoundingStrategy()));
		
		assertEquals (new BigDecimal(299.5) , m.getProductType().getQuantityOnHand().getAmount());
	}
	
	@Test
	public void testEquals() {
		TestMeasuredProductInstance m = klein1;
		
		assertEquals(m,klein1);
		
	}
	
	@Test
	public void testNotEquals1() {
		
		assertNotSame(klein2, klein1);
	}

	@Test
	public void testNotEquals2() {
		
		assertNotSame(gross, klein1);
	}
	
	@Test
	public void testEqualsMethod(){
	
		TestMeasuredProductInstance m = klein1;
		
		assertTrue(klein1.equals(klein1));
		assertTrue(klein1.equals(m));
		assertFalse(klein1.equals(klein2));
		assertFalse(klein1.equals(gross));
	}
	
	@Test 
	public void testHashcode1() {
		
		assertEquals(klein1.hashCode(),klein2.hashCode());
	}
	
	@Test 
	public void testHashcode2() {
		
		assertNotSame(klein1.hashCode(),gross.hashCode());
	}
}
