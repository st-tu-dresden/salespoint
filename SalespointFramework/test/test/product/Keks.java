package test.product;

import javax.persistence.Entity;

import org.salespointframework.core.product.PersistentProduct;

@SuppressWarnings("javadoc")
@Entity
public class Keks extends PersistentProduct {
	
	@Deprecated
	public Keks() {
		
	}
	
	public Keks(KeksType keks) {
		super(keks);
	}
	
}
