package test.product;

import javax.persistence.Entity;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.PersistentProduct;

@SuppressWarnings("javadoc")
@Entity
public class KeksType extends PersistentProduct {

	@Deprecated
	protected KeksType() {
		
	}
	
	public KeksType(String name, Money price) {
		super(name, price);
	}

}
