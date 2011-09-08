package test.product;

import javax.persistence.Entity;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.PersistentProductType;

@Entity
public class KeksProduct extends PersistentProductType {

	@Deprecated
	protected KeksProduct() {
		
	}
	
	public KeksProduct(String name, Money price) {
		super(name, price);
	}

}
