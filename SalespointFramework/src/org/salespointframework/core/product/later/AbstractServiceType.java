package org.salespointframework.core.product.later;

import org.joda.time.DateTime;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.AbstractProductType;
import org.salespointframework.core.shop.Shop;
import org.salespointframework.util.Objects;

public abstract class AbstractServiceType extends AbstractProductType implements ServiceType{

	protected long startOfPeriodOfOperation;
	protected long endOfPeriodOfOperation;
	
	@Deprecated
	public AbstractServiceType(){
	}
	
	public AbstractServiceType( String name, Money price){
		super( name, price);
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(price, "price");
		
		this.startOfPeriodOfOperation = Shop.INSTANCE.getTime().getDateTime().getMillis();
		this.endOfPeriodOfOperation = Shop.INSTANCE.getTime().getDateTime().plusYears(100).getMillis();
	}
	
	public AbstractServiceType(String name, Money price, DateTime start, DateTime end){
		super( name, price);
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(price, "price");
		Objects.requireNonNull(start, "start");
		Objects.requireNonNull(end, "end");
		
		this.startOfPeriodOfOperation=start.getMillis();
		this.endOfPeriodOfOperation=end.getMillis();
	}
	
	public DateTime getStartOfPeriodOfOperation(){
		return new DateTime(startOfPeriodOfOperation);
	}
	
	public DateTime getEndOfPeriodOfOperation(){
		return new DateTime(endOfPeriodOfOperation);
	}
	
	
	
}
