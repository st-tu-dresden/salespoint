package org.salespointframework.core.money;

public interface RoundingStrategy {
	public Quantity round(Quantity quantity);
}
