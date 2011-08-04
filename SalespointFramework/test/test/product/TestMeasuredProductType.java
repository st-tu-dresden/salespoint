package test.product;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.later.AbstractMeasuredProductType;
import org.salespointframework.core.quantity.Quantity;

public class TestMeasuredProductType extends AbstractMeasuredProductType{
	
	@Deprecated
	protected TestMeasuredProductType(){
		
	}
	
	public TestMeasuredProductType(String name, Money price, Quantity quantityOnHand){
		super( name, price, quantityOnHand);
	}

}
