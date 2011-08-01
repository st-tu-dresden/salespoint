package test.product.feature;

import static org.junit.Assert.*;

import org.junit.Test;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.features.*;
import org.salespointframework.util.ArgumentNullException;

public class ProductFeatureTest {
	
	@Test(expected=ArgumentNullException.class)
	public void testNullcheckName() {
		ProductFeature.create(null, Money.ZERO);
	}
	
	@Test(expected=ArgumentNullException.class)
	public void testNullcheckPrice() {
		ProductFeature.create("", null);
	}

	@Test
	public void testGetName() {
		ProductFeature pf = ProductFeature.create("awesome", Money.ZERO);
		
		assertEquals("awesome", pf.getName());
	}
	
	@Test
	public void testGetPrice() {
		ProductFeature pf = ProductFeature.create("awesome", Money.ZERO);
		
		assertEquals(Money.ZERO, pf.getPrice());
	}
	
	@Test 
	public void testEquals1() {
		ProductFeature pf1 = ProductFeature.create("awesome", Money.ZERO);
		ProductFeature pf2 = pf1;
		
		assertEquals(pf1, pf2);
	}
	
	@Test 
	public void testEquals2() {
		ProductFeature pf1 = ProductFeature.create("awesome", Money.ZERO);
		ProductFeature pf2 = ProductFeature.create("awesome", Money.ZERO);
		
		assertEquals(pf1, pf2);
	}
	
	@Test 
	public void testNotEquals1() {
		ProductFeature pf1 = ProductFeature.create("awesome", Money.ZERO);
		ProductFeature pf2 = ProductFeature.create("awesome", new Money(1));
		
		assertNotSame(pf1, pf2);
	}
	
	@Test 
	public void testNotEquals2() {
		ProductFeature pf1 = ProductFeature.create("awesome", Money.ZERO);
		ProductFeature pf2 = ProductFeature.create("Awesome", Money.ZERO);
		
		assertNotSame(pf1, pf2);
	}
	
	@Test
	public void testEquals(){
		
		ProductFeature pf1 = ProductFeature.create("awesome", Money.ZERO);
		ProductFeature pf2 = ProductFeature.create("awesome", Money.ZERO);
		ProductFeature pf3 = ProductFeature.create("other", Money.ZERO);
		ProductFeature pf4 = pf1;
		
		assertTrue(pf1.equals(pf1));
		assertTrue(pf1.equals(pf4));
		assertTrue(pf1.equals(pf2));
		assertFalse(pf1.equals(pf3));
	}
	
	@Test 
	public void testHashcode1() {
		ProductFeature pf1 = ProductFeature.create("awesome", Money.ZERO);
		ProductFeature pf2 = ProductFeature.create("awesome", Money.ZERO);
		
		assertEquals(pf1.hashCode(),pf2.hashCode());
	}
	
	@Test 
	public void testHashcode2() {
		ProductFeature pf1 = ProductFeature.create("awesome", Money.ZERO);
		ProductFeature pf2 = ProductFeature.create("other", Money.ZERO);
		
		assertNotSame(pf1.hashCode(),pf2.hashCode());
	}
}
