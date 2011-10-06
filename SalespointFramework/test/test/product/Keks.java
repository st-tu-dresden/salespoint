package test.product;

import javax.persistence.Entity;

import org.salespointframework.core.product.PersistentProduct;
import org.salespointframework.util.Tuple;

@SuppressWarnings("javadoc")
@Entity
public class Keks extends PersistentProduct {
	
	@Deprecated
	public Keks() {
		
	}
	
	public Keks(KeksType keks) {
		super(keks);
	}
	
   public Keks(KeksType productType, Iterable<Tuple<String, String>> features) {
       super(productType, features);
   }

	
}
