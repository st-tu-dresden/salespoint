package dvdshop.model;

import org.salespointframework.core.catalog.PersistentCatalog;
import org.salespointframework.core.product.ProductIdentifier;



public class VideoCatalog extends PersistentCatalog {

	
	public Dvd getDvd(ProductIdentifier productIdentifier) {
		return super.get(Dvd.class, productIdentifier);
	}
	
	public BlueRay getBlueRay(ProductIdentifier productIdentifier) {
		return super.get(BlueRay.class, productIdentifier);
	}
	
	public Iterable<Dvd> findDvds() {
		return super.find(Dvd.class);
	}
	
	public Iterable<BlueRay> findBlueRays() {
		return super.find(BlueRay.class);
	}
	

	
}
