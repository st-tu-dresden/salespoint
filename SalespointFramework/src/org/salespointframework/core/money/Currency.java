package org.salespointframework.core.money;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("CURRENCY")
public class Currency extends Metric {
	
	private String alphabeticCode;
	private String majorUnitSymbol;
	private String minorUnitSymbol;
	private float ratioOfMinorUnitToMajorUnit;
	
	/** Protected class constructor.
	 * 
	 * This constructor is required for JPA/Hibernate.
	 * To instantiate objects, use public constructors instead.
	 */
	@Deprecated
	protected Currency() {};
	
	/** Class constructor 
	 * 
	 * @param name                        Name of this currency, e.g. "Euro"
	 * @param alphabeticCode              Alphabetic representation of this currency, e.g. "EUR"
	 * @param majorUnitSymbol             Symbol denoting the major unit of this currency, e.g. "�"
	 * @param minorUnitSymbol             Symbol denoting the minor unit of this currency, e.g. "ct"
	 * @param ratioOfMinorUnitToMajorUnit Ratio of the minor unit to the major unit, e.g. 100ct are 1� so 100.
	 * @param definition                  Definition of this currency, e.g. "the euro is the official currency of the eurozone."
	 */
	public Currency(String name, String alphabeticCode, String majorUnitSymbol,
			String minorUnitSymbol, float ratioOfMinorUnitToMajorUnit,
			String definition) {
		super(name, majorUnitSymbol, definition);
		this.alphabeticCode = alphabeticCode;
		this.majorUnitSymbol = majorUnitSymbol;
		this.minorUnitSymbol = minorUnitSymbol;
		this.ratioOfMinorUnitToMajorUnit = ratioOfMinorUnitToMajorUnit;
	}
	
	/** Returns the alphabetic code of this currency.
	 * 
	 * @return the alpahbetic code of this currency.
	 */
	public String getAlphabeticCode() {
		return alphabeticCode;
	}
	
	/** Returns the major unit symbol for this currency.
	 * 
	 * @return the major unit symbol for this currency.
	 */
	public String getMajorUnitSymbol() {
		return majorUnitSymbol;
	}
	
	/** Returns the minor unit symbol for this currency.
	 * 
	 * @return the minor unit symbol for this currency.
	 */
	public String getMinorUnitSymbol() {
		return minorUnitSymbol;
	}
	
	/** Returns the ratio of the minor unit to the major unit of this currency.
	 * 
	 * @return the ratio of minor unit to major unit of this currency.
	 */
	public float getRatioOfMinorUnitToMajorUnit() {
		return ratioOfMinorUnitToMajorUnit;
	}
	
}
