package test.product;

import java.math.BigDecimal;

import org.salespointframework.core.product.measured.MeasuredProduct;
import org.salespointframework.core.product.measured.PersistentMeasuredProductInstance;
import org.salespointframework.core.quantity.Quantity;

@SuppressWarnings("javadoc")
public class TestMeasuredProductInstance extends PersistentMeasuredProductInstance{

	@Deprecated
	public TestMeasuredProductInstance(){
		
	}
	
	public TestMeasuredProductInstance(MeasuredProduct productType, Quantity quantity) {
		super(productType, quantity);
	}
	
	public TestMeasuredProductInstance(MeasuredProduct productType, int amount) {
		super(productType, amount);
	}
	
	public TestMeasuredProductInstance(MeasuredProduct productType, float amount) {
		super(productType, amount);
	}
	
	public TestMeasuredProductInstance(MeasuredProduct productType, long amount) {
		super(productType, amount);
	}
	
	public TestMeasuredProductInstance(MeasuredProduct productType, BigDecimal amount) {
		super(productType, amount);
	}
	
	public TestMeasuredProductInstance(MeasuredProduct productType, double amount) {
		super(productType, amount);
	}

}
