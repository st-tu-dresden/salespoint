package org.salespointframework.catalog;

import javax.persistence.Entity;

import org.javamoney.moneta.Money;
import org.salespointframework.core.Currencies;

@Entity
@SuppressWarnings("serial")
public class Cookie extends Product {

	String property;

	@Deprecated
	Cookie() {}

	public Cookie(String name, Money price) {
		super(name, Money.of(9001, Currencies.EURO));
	}
}
