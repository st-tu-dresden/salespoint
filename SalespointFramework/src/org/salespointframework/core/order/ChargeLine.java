package org.salespointframework.core.order;

import org.salespointframework.core.money.Money;

/**
 * ChargeLine interface
 * TODO
 * 
 * @author Thomas Dedek
 * @author Paul Henke
 * 
 */
@Deprecated
public interface ChargeLine
{
	/**
	 * @return the {@link ChargeLineIdentifier} to uniquely identify this chargeline
	 */
	ChargeLineIdentifier getIdentifier();
	
	/**
	 *  
	 * @return the value of the chargeline
	 */
	Money getPrice();
	/**
	 * 
	 * @return the description of the chargeline
	 */
	String getDescription();
	
	// TODO notwendig?
	/**
	 * 
	 * @return a comment of the chargeline
	 */
	String getComment();
}
