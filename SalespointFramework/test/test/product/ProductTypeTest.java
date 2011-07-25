package test.product;

import static org.junit.Assert.*;

import org.junit.Test;
import org.salespointframework.core.money.Money;

public class ProductTypeTest {

	@Test
	public void testEquals1() {
		KeksProduct k1 = new KeksProduct("wurst", Money.ZERO);
		KeksProduct k2 = k1;
		
		assertEquals(k2,k1);
		
	}
	
	@Test
	public void testEquals2() {
		KeksProduct k1 = new KeksProduct("wurst", Money.ZERO);
		KeksProduct k2 = new KeksProduct("wurst", Money.ZERO);
		
		assertEquals(k2,k1);
	}
	
	@Test
	public void testNotEquals1() {
		KeksProduct k1 = new KeksProduct("wurst", Money.ZERO);
		KeksProduct k2 = new KeksProduct("wurst", new Money(1));
		
		assertNotSame(k2, k1);
	}
	
	@Test
	public void testNotEquals2() {
		KeksProduct k1 = new KeksProduct("wurst", Money.ZERO);
		KeksProduct k2 = new KeksProduct("Wurst", Money.ZERO);
		
		assertNotSame(k2, k1);
	}
	
	// TODO weitere Tests
	

}
