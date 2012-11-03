package videoshop.model;

import org.salespointframework.core.catalog.PersistentCatalog;
import org.salespointframework.core.product.ProductIdentifier;

public class VideoCatalog extends PersistentCatalog {
	
	public Disc getDisc(ProductIdentifier productIdentifier) {
		return super.get(Disc.class, productIdentifier);
	}
	
	public Iterable<Dvd> findDvds() {
		return super.find(Dvd.class);
	}
	
	public Iterable<BluRay> findBluRays() {
		return super.find(BluRay.class);
	}
}
