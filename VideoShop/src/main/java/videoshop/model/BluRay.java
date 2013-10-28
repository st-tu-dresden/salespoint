package videoshop.model;

import javax.persistence.Entity;

import org.salespointframework.core.money.Money;

// (｡◕‿◕｡)
// siehe Disc

@Entity
public class BluRay extends Disc {
	
	@Deprecated
	public BluRay() {}
	
	public BluRay(String name, String image, Money price, String genre) {
		super(name, image, price, genre);
	}
}
