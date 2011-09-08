package test.product;

import javax.persistence.Entity;

import org.salespointframework.core.product.PersistentProduct;

@Entity
public class KeksInstance extends PersistentProduct {
	
	@Deprecated
	public KeksInstance() {
		
	}
	
	public KeksInstance(KeksProduct keks) {
		super(keks);
	}
	
}
