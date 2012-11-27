package videoshop.model;

import org.salespointframework.core.catalog.PersistentCatalog;
import org.salespointframework.core.product.ProductIdentifier;

public class VideoCatalog {
	
	private PersistentCatalog catalog = new PersistentCatalog();
	
	public void add(Disc disc) {
		catalog.add(disc);
	}
	
	public Disc getDisc(ProductIdentifier productIdentifier) {
		return catalog.get(Disc.class, productIdentifier);
	}
	
	public Iterable<Disc> findAll() {
		return catalog.find(Disc.class);
	}
	
	public Iterable<Dvd> findDvds() {
		return catalog.find(Dvd.class);
	}
	
	public Iterable<BluRay> findBluRays() {
		return catalog.find(BluRay.class);
	}
	
	public void update(Disc disc) {
		catalog.update(disc);
	}
}
