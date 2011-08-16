package org.salespointframework.core.product.later;

import java.util.Date;

import org.joda.time.DateTime;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.AbstractProductType;
import org.salespointframework.core.shop.Shop;
import org.salespointframework.util.Objects;

/**
 * 
 * This is an abstract representation of a ServiceType which provides basic
 * functionality
 * 
 */

//TODO
//@Entity
public abstract class AbstractServiceType extends AbstractProductType implements ServiceType{

	
	//TODO long -> Date
	protected Date startOfPeriodOfOperation;
	protected Date endOfPeriodOfOperation;
	
	@Deprecated
	protected AbstractServiceType(){
	}
	
	/**
     * Parameterized constructor with 
     * @param name The name of the ServiceType
     * @param price The price of the ServiceType <br><br>
     * The Executing of the ServiceType always is possible.
     * The start of the ServiceType is now. 
     * 
     */
	public AbstractServiceType( String name, Money price){
		super( name, price);
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(price, "price");
		
		this.startOfPeriodOfOperation = Shop.INSTANCE.getTime().getDateTime().toDate();
		this.endOfPeriodOfOperation = Shop.INSTANCE.getTime().getDateTime().plusYears(100).toDate();
	}
	
	/**
     * Parameterized constructor with 
     * @param name The name of the ServiceType
     * @param price The price of the ServiceType
     * @param start The start of the ServiceType.
     * @param end The end of the ServiceType <br><br>
     * After the <b>start</b> and before the <b>end</b> you can create ServiceInstances of this ServiceType
     * @throws IllegalArgumentException
     *             if the start is after the end
     * @throws IllegalArgumentException
     *             if the start is before now
     * @throws IllegalArgumentException
     *             if the end is before now
     */
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

		this.startOfPeriodOfOperation=start.toDate();
		this.endOfPeriodOfOperation=end.toDate();
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
	
	/**
	 * Determines if the given {@link ServiceType} is equal to this one or
	 * not. Two ServiceTypes are equal to each other, if their hash code is
	 * the same.
	 * 
	 * @param other
	 *            this one should be compared with
	 * @return <code>true</code> if and only if the hashCode of this Object
	 *         equals the hashCode of the object given as parameter.
	 *         <code>false</code> otherwise.
	 */
	
	public final boolean equals(ServiceType other) {
		if(other == null) return false;
		if(other == this) return true;
		return this.getProductIdentifier().equals(other.getProductIdentifier());
	}
}
