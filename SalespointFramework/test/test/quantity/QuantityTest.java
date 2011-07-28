package test.quantity;

import org.junit.Test;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.quantity.Metric;
import org.salespointframework.core.quantity.Quantity;

public class QuantityTest {

	@Test
	public void multiply() {
		Quantity q = new Quantity(5, Metric.PIECES, Quantity.ROUND_ONE);
		Money m = new Money(2);
		Money r = q.multiply_(m);
		System.out.println("Result: " + r.toString());
	}
}
