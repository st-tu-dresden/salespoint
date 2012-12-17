package test.quantity;

import static org.junit.Assert.*;

import org.junit.Test;
import org.salespointframework.core.money.Money;

@SuppressWarnings("javadoc")
public class MoneyTest
{
	@Test
	public void multiplyByZero() {
		Money m = Money.ZERO;
		m = m.multiply(new Money(10));
		System.out.println(m);
		
		assertEquals(m, Money.ZERO);
	}
	
	@Test
	public void multiplyByZero2() {
		Money m = Money.ZERO;
		m = m.multiply(Money.ZERO);
		System.out.println(m);
		
		assertEquals(m, Money.ZERO);
	}
	
	@Test
	public void scaleTest() {
		Money m = new Money(1.99);
		m = m.multiply(new Money(1.5));
		
		System.out.println(m);
	}
}
