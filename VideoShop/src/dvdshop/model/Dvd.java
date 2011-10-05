package dvdshop.model;

import javax.persistence.Entity;

import org.salespointframework.core.money.Money;

@Entity
public class Dvd extends Disc {
	@Deprecated
	protected Dvd() {}
	
	public Dvd(String name, Money price, String genre) {
		super(name, price, genre);
	}
}
