package dvdshop.model;

import org.salespointframework.core.catalog.PersistentCatalog;
import org.salespointframework.core.product.ProductIdentifier;
import org.springframework.stereotype.Component;

@Component
public class VideoCatalog extends PersistentCatalog {
	
	public Disc getDisc(ProductIdentifier productIdentifier) {
		return super.get(Disc.class, productIdentifier);
	}
	
	public Iterable<Dvd> findDvds() {
		return super.find(Dvd.class);
	}
	
	public Iterable<BluRay> findBlueRays() {
		return super.find(BluRay.class);
	}
}
