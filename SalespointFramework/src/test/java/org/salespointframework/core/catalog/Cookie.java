package org.salespointframework.core.catalog;

import javax.persistence.Entity;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.salespointframework.core.quantity.Units;

@SuppressWarnings("javadoc")
@Entity
public class Cookie extends Product {

	String property; 
	
	@Deprecated
	protected Cookie() {
		
	}
	
	public Cookie(String name, Money price) {
		super(name, Money.of(CurrencyUnit.EUR, 9001), Units.METRIC);
	}
}
