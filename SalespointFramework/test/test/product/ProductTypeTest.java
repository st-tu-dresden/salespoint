package test.product;

import static org.junit.Assert.*;

import org.junit.Test;
import org.salespointframework.core.money.Money;

public class ProductTypeTest {

	@Test
	public void testEquals1() {
		Keks k1 = new Keks("wurst", Money.ZERO);
		Keks k2 = k1;
		
		assertEquals(k2,k1);
		
	}
	
	@Test
	public void testEquals2() {
		Keks k1 = new Keks("wurst", Money.ZERO);
		Keks k2 = new Keks("wurst", Money.ZERO);
		
		assertEquals(k2,k1);
	}
	
	@Test
	public void testNotEquals1() {
		Keks k1 = new Keks("wurst", Money.ZERO);
		Keks k2 = new Keks("wurst", new Money(1));
		
		assertNotSame(k2, k1);
	}
	
	@Test
	public void testNotEquals2() {
		Keks k1 = new Keks("wurst", Money.ZERO);
		Keks k2 = new Keks("Wurst", Money.ZERO);
		
		assertNotSame(k2, k1);
	}
	
	// TODO weitere Tests
	

}
