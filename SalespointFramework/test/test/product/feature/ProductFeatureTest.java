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
	

	// TODO hashcode check
	
}
