package test.product;

import javax.persistence.Entity;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.PersistentProductType;

@SuppressWarnings("javadoc")
@Entity
public class KeksType extends PersistentProductType {

	@Deprecated
	protected KeksType() {
		
	}
	
	public KeksType(String name, Money price) {
		super(name, price);
	}

}
