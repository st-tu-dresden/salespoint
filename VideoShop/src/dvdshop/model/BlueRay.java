package dvdshop.model;

import javax.persistence.Entity;

import org.salespointframework.core.money.Money;

@Entity
public class BlueRay extends Disc {
	
	@Deprecated
	public BlueRay() {
		
	}
	
	public BlueRay(String name, Money price, String genre) {
		super(name, price, genre);
	}

}
