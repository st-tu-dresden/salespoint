package org.salespointframework.core.product.later;

import org.joda.time.DateTime;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.AbstractProductType;

public abstract class AbstractServiceType extends AbstractProductType{

	private DateTime startOfPeriodOfOperation;
	private DateTime endOfPeriodOfOperation;
	
	@Deprecated
	public AbstractServiceType(){
	}
	
	public AbstractServiceType( String name, Money price){
		super( name, price);
	}
	
	public AbstractServiceType(String name, Money price, DateTime start, DateTime end){
		super( name, price);
		this.startOfPeriodOfOperation=start;
		this.endOfPeriodOfOperation=end;
	}
	
	public DateTime getStartOfPeriodOfOperation(){
		return this.startOfPeriodOfOperation;
	}
	
	public DateTime getEndOfPeriodOfOperation(){
		return this.endOfPeriodOfOperation;
	}
	
	
	
}
