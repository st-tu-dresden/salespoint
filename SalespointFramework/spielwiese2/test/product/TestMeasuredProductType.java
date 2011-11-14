package test.product;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.measured.PersistentMeasuredProduct;
import org.salespointframework.core.quantity.Quantity;

@SuppressWarnings("javadoc")
public class TestMeasuredProductType extends PersistentMeasuredProduct{
	
	@Deprecated
	protected TestMeasuredProductType(){
		
	}
	
	public TestMeasuredProductType(String name, Money price, Quantity quantityOnHand){
		super( name, price, quantityOnHand);
	}

}
