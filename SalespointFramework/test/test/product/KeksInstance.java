package test.product;

import javax.persistence.Entity;

import org.salespointframework.core.product.AbstractProductInstance;

@Entity
public class KeksInstance extends AbstractProductInstance {
	
	@Deprecated
	public KeksInstance() {
		
	}
	
	public KeksInstance(KeksProduct keks) {
		super(keks);
	}
	
}
