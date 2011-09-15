package org.salespointframework.core.order;

import org.salespointframework.core.money.Money;

/**
 * 
 * @author Thomas Dedek
 * @author Paul Henke
 *
 */
public interface ChargeLine {
	ChargeLineIdentifier getChargeLineIdentifier();
	Money getPrice();
	String getDescription();
	String getComment();
}
