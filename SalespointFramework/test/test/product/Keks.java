package test.product;

import javax.persistence.Entity;

import org.salespointframework.core.product.PersistentProduct;
import org.salespointframework.core.product.ProductFeatureIdentifier;

@SuppressWarnings("javadoc")
@Entity
public class Keks extends PersistentProduct {
	
	@Deprecated
	public Keks() {
		
	}
	
	public Keks(KeksType keks) {
		super(keks);
	}
	
   public Keks(KeksType productType, ProductFeatureIdentifier... features) {
       super(productType, features);
   }

	
}
