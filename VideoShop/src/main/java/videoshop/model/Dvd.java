package videoshop.model;

import javax.persistence.Entity;

import org.salespointframework.core.money.Money;

// (｡◕‿◕｡)
// siehe Disc

@Entity
public class Dvd extends Disc {
	
	@Deprecated
	protected Dvd() {}
	
	public Dvd(String name, String image, Money price, String genre) {
		super(name, image, price, genre);
	}
}
