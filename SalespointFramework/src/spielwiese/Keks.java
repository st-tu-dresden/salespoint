package spielwiese;

import javax.persistence.Entity;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.AbstractProductType;

@Entity
public class Keks extends AbstractProductType {

	@Deprecated
	protected Keks() {
		
	}
	
	public Keks(String name, Money price ) {
		super(name, price);
	}

}
