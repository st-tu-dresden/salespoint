package org.salespointframework.catalog;

import javax.persistence.Entity;

import org.javamoney.moneta.Money;
import org.salespointframework.core.Currencies;
import org.salespointframework.quantity.Units;

@SuppressWarnings("javadoc")
@Entity
public class Cookie extends Product {

	String property;

	@Deprecated
	protected Cookie() {

	}

	public Cookie(String name, Money price) {
		super(name, Money.of(9001, Currencies.EURO), Units.METRIC);
	}
}
