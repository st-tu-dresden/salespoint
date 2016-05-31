package org.salespointframework.catalog;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;

import org.javamoney.moneta.Money;
import org.salespointframework.core.Currencies;

@Entity
@SuppressWarnings("serial")
public class Cookie extends Product {

	String property;

	@Deprecated
	Cookie() {}

	public Cookie(String name, MonetaryAmount price) {
		super(name, Money.of(9001, Currencies.EURO));
	}
}
