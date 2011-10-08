package dvdshop.model;

import org.salespointframework.core.catalog.PersistentCatalog;
import org.salespointframework.core.product.ProductTypeIdentifier;


public class VideoCatalog extends PersistentCatalog {

	
	public Dvd getDvd(ProductTypeIdentifier productTypeIdentifier) {
		return super.get(Dvd.class, productTypeIdentifier);
	}
	
	public BlueRay getBlueRay(ProductTypeIdentifier productTypeIdentifier) {
		return super.get(BlueRay.class, productTypeIdentifier);
	}
	
	public Iterable<Dvd> findDvds() {
		return super.find(Dvd.class);
	}
	
	public Iterable<BlueRay> findBlueRays() {
		return super.find(BlueRay.class);
	}
	

	
}
