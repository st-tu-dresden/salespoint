package test.product;

import static org.junit.Assert.*;

import org.junit.Test;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.AbstractProductType;
import org.salespointframework.core.product.ProductType;
import org.salespointframework.core.product.features.ProductFeature;
import org.salespointframework.util.ArgumentNullException;

public class ProductTypeTest {

	private KeksProduct k1 = new KeksProduct("keks", Money.ZERO);
	private KeksProduct k2 = new KeksProduct("keks", Money.ZERO);
	private KeksProduct k3 = new KeksProduct("keks", new Money(20));
	@Test
	public void testEquals1() {
		KeksProduct k4 = k1;
		
		assertEquals(k4,k1);
		
	}
	
	@Test
	public void testEquals2() {
		
		assertEquals(k2.getName(),k1.getName());
		assertEquals(k2.getPrice(),k1.getPrice());
	
	}
	
	@Test
	public void testNotEquals1() {
		KeksProduct k4 = new KeksProduct("wurst", new Money(1));
		
		assertNotSame(k4, k1);
	}
	
	@Test
	public void testNotEquals2() {
		
		assertNotSame(k2, k1);
	}
	
	@Test(expected=ArgumentNullException.class)
	public void testNotNullName() {
		@SuppressWarnings("unused")
		KeksProduct k = new KeksProduct(null, Money.ZERO);
	}
	
	@Test(expected=ArgumentNullException.class)
	public void testNotNullPrice() {
		@SuppressWarnings("unused")
		KeksProduct k = new KeksProduct("keks", null);
	}
	
	@Test
	public void testTypeName(){
		
		assertEquals("keks", k1.getName());
	}

	@Test
	public void testTypePrice(){
		
		assertEquals(Money.ZERO, k1.getPrice());
	}
	
	@Test
	public void testEquals(){
	
		KeksProduct k4 = k1;
		
		assertTrue(k1.equals(k1));
		assertTrue(k1.equals(k4));
		assertFalse(k1.equals(k2));
		assertFalse(k1.equals(k3));
	}
	
}
