package org.salespointframework.core.product.later;

import org.joda.time.DateTime;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.AbstractProductType;
import org.salespointframework.core.product.ProductType;
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
		
		 if (start.isAfter(end))
	            throw new IllegalArgumentException("A serviceType cannot end before it starts.");

		 if (start.isBefore(Shop.INSTANCE.getTime().getDateTime().minusMillis(500))==true)
			 	throw new IllegalArgumentException("A serviceType cannot start before its creation time.");

		 if (end.isBefore(Shop.INSTANCE.getTime().getDateTime().minusMillis(500))==true)
			 	throw new IllegalArgumentException("A serviceType cannot end before its creation time.");

		this.startOfPeriodOfOperation=start.getMillis();
		this.endOfPeriodOfOperation=end.getMillis();
	}
	
	public DateTime getStartOfPeriodOfOperation(){
		return new DateTime(startOfPeriodOfOperation);
	}
	
	public DateTime getEndOfPeriodOfOperation(){
		return new DateTime(endOfPeriodOfOperation);
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(other == this) return true;
		if(!(other instanceof ServiceType)) return false;
		return this.equals((ServiceType)other);
	}
	
	public boolean equals(ServiceType other) {
		if(other == null) return false;
		if(other == this) return true;
		return productIdentifier.equals(other.getProductIdentifier());
	}
	
	@Override
	public int hashCode() {
		return this.getProductIdentifier().hashCode();
	}
	
}
