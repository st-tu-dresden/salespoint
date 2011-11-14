package test.product;

import javax.persistence.Entity;

import org.salespointframework.core.product.PersistentProductInstance;
import org.salespointframework.core.product.ProductFeatureIdentifier;

@SuppressWarnings("javadoc")
@Entity
public class KeksInstance extends PersistentProductInstance {
	
	@Deprecated
	public KeksInstance() {
		
	}
	
	public KeksInstance(Keks keks) {
		super(keks);
	}
	
   public KeksInstance(Keks productType, ProductFeatureIdentifier... features) {
       super(productType, features);
   }

	
}
