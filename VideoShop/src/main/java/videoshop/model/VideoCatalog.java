package videoshop.model;

import org.salespointframework.core.catalog.Catalog;
import org.salespointframework.core.catalog.ProductIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VideoCatalog {

	private final Catalog catalog;

	/**
	 * @param catalog
	 */
	@Autowired
	public VideoCatalog(Catalog catalog) {
		this.catalog = catalog;
	}

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
