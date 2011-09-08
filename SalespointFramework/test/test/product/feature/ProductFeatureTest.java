package test.product.feature;

import static org.junit.Assert.*;

import org.junit.Test;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.features.*;
import org.salespointframework.util.ArgumentNullException;

public class ProductFeatureTest {
	
	@Test(expected=ArgumentNullException.class)
	public void testNullcheckName() {
		ProductFeature_old.create(null, Money.ZERO);
	}
	
	@Test(expected=ArgumentNullException.class)
	public void testNullcheckPrice() {
		ProductFeature_old.create("", null);
	}

	@Test
	public void testGetName() {
		ProductFeature_old pf = ProductFeature_old.create("awesome", Money.ZERO);
		
		assertEquals("awesome", pf.getName());
	}
	
	@Test
	public void testGetPrice() {
		ProductFeature_old pf = ProductFeature_old.create("awesome", Money.ZERO);
		
		assertEquals(Money.ZERO, pf.getPrice());
	}
	
	@Test 
	public void testEquals1() {
		ProductFeature_old pf1 = ProductFeature_old.create("awesome", Money.ZERO);
		ProductFeature_old pf2 = pf1;
		
		assertEquals(pf1, pf2);
	}
	
	@Test 
	public void testEquals2() {
		ProductFeature_old pf1 = ProductFeature_old.create("awesome", Money.ZERO);
		ProductFeature_old pf2 = ProductFeature_old.create("awesome", Money.ZERO);
		
		assertEquals(pf1, pf2);
	}
	
	@Test 
	public void testNotEquals1() {
		ProductFeature_old pf1 = ProductFeature_old.create("awesome", Money.ZERO);
		ProductFeature_old pf2 = ProductFeature_old.create("awesome", new Money(1));
		
		assertNotSame(pf1, pf2);
	}
	
	@Test 
	public void testNotEquals2() {
		ProductFeature_old pf1 = ProductFeature_old.create("awesome", Money.ZERO);
		ProductFeature_old pf2 = ProductFeature_old.create("Awesome", Money.ZERO);
		
		assertNotSame(pf1, pf2);
	}
	
	@Test
	public void testEquals(){
		
		ProductFeature_old pf1 = ProductFeature_old.create("awesome", Money.ZERO);
		ProductFeature_old pf2 = ProductFeature_old.create("awesome", Money.ZERO);
		ProductFeature_old pf3 = ProductFeature_old.create("other", Money.ZERO);
		ProductFeature_old pf4 = pf1;
		
		assertTrue(pf1.equals(pf1));
		assertTrue(pf1.equals(pf4));
		assertTrue(pf1.equals(pf2));
		assertFalse(pf1.equals(pf3));
	}
	
	@Test 
	public void testHashcode1() {
		ProductFeature_old pf1 = ProductFeature_old.create("awesome", Money.ZERO);
		ProductFeature_old pf2 = ProductFeature_old.create("awesome", Money.ZERO);
		
		assertEquals(pf1.hashCode(),pf2.hashCode());
	}
	
	@Test 
	public void testHashcode2() {
		ProductFeature_old pf1 = ProductFeature_old.create("awesome", Money.ZERO);
		ProductFeature_old pf2 = ProductFeature_old.create("other", Money.ZERO);
		
		assertNotSame(pf1.hashCode(),pf2.hashCode());
	}
}
