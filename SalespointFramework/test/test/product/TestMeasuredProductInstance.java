package test.product;

import java.math.BigDecimal;

import org.salespointframework.core.product.later.AbstractMeasuredProductInstance;
import org.salespointframework.core.product.later.MeasuredProductType;
import org.salespointframework.core.quantity.Quantity;

public class TestMeasuredProductInstance extends AbstractMeasuredProductInstance{

	@Deprecated
	public TestMeasuredProductInstance(){
		
	}
	
	public TestMeasuredProductInstance(MeasuredProductType productType, Quantity quantity) {
		super(productType, quantity);
	}
	
	public TestMeasuredProductInstance(MeasuredProductType productType, int amount) {
		super(productType, amount);
	}
	
	public TestMeasuredProductInstance(MeasuredProductType productType, float amount) {
		super(productType, amount);
	}
	
	public TestMeasuredProductInstance(MeasuredProductType productType, long amount) {
		super(productType, amount);
	}
	
	public TestMeasuredProductInstance(MeasuredProductType productType, BigDecimal amount) {
		super(productType, amount);
	}
	
	public TestMeasuredProductInstance(MeasuredProductType productType, double amount) {
		super(productType, amount);
	}

}
