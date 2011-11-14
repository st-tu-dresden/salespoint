package test.product;

import javax.persistence.Entity;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.PersistentProduct;

@SuppressWarnings("javadoc")
@Entity
public class Keks extends PersistentProduct {

	@Deprecated
	protected Keks() {
		
	}
	
	public Keks(String name, Money price) {
		super(name, price);
	}

}
