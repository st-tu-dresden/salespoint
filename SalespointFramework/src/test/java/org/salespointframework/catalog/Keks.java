package org.salespointframework.catalog;

import javax.persistence.Entity;

import org.salespointframework.core.catalog.Product;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.quantity.Units;

@SuppressWarnings("javadoc")
@Entity
public class Keks extends Product {

	String property; 
	
	@Deprecated
	protected Keks() {
		
	}
	
	public Keks(String name, Money price) {
		super(name, Money.OVER9000, Units.METRIC);
	}
}
