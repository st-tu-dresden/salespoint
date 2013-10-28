package videoshop.model;

import org.salespointframework.core.catalog.Catalog;
import org.salespointframework.core.catalog.ProductIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// (｡◕‿◕｡)
// Der Salespoint Katalog ist sehr allgemein gehalten, er nimmt Products und gibt Products zurück
// Zusätzlich hat er einige Methoden, welche hier unnötig sind, deswegen ist es sinnvoll einen spezielleren Katalog zu schreiben,
// welcher auf den vorhanden Salespoint-Catalog delegiert und die immer gleichen Paramter übergibt.
// Merke: Nirgends wird Product erwähnt, es gehen nur Discs rein und Dvds oder Blurays raus

// @Component sorgt dafür, dass wir unseren VideoCatalog später auch per @Autowired bekommen können

@Component
public class VideoCatalog {

	private final Catalog catalog;

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
