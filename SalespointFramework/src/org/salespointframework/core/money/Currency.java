package org.salespointframework.core.money;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Currency extends Metric {
	
	private String alphabeticCode;
	private String majorUnitSymbol;
	private String minorUnitSymbol;
	private float ratioOfMinorUnitToMajorUnit;
	
	public String getAlphabeticCode() {
		return alphabeticCode;
	}
	public String getMajorUnitSymbol() {
		return majorUnitSymbol;
	}
	public String getMinorUnitSymbol() {
		return minorUnitSymbol;
	}
	public float getRatioOfMinorUnitToMajorUnit() {
		return ratioOfMinorUnitToMajorUnit;
	}
	
}
