package org.salespointframework.core.product.later;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;

import org.joda.time.DateTime;
import org.salespointframework.core.product.AbstractProductInstance;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.product.ProductInstance;
import org.salespointframework.core.product.SerialNumber;
import org.salespointframework.core.shop.Shop;
import org.salespointframework.util.Objects;

/**
 * 
 * This is an abstract representation of a ServiceInstance which provides basic
 * functionality
 * 
 */

public abstract class AbstractServiceInstance extends AbstractProductInstance implements ServiceInstance {
	
	private ServiceType serviceType;
	
	private DateTime scheduledStart;
	private DateTime scheduledEnd;
	private ServiceDeliveryStatus serviceDeliveryStatus;
	
	@Deprecated
	public AbstractServiceInstance(){}
	
	/**
     * Parameterized constructor with 
     * @param serviceType The {@link ServiceType} of this ServiceInstance
     * @param start The start time of the ServiceInstance.
     * @param end The end time of the ServiceInstance.
     *	@throws IllegalArgumentException
     *             if the start is after the end
     * @throws IllegalArgumentException
     *             if the start is before the start of the ServiceType
     * @throws IllegalArgumentException
     *             if the end is after the end of the ServiceType
     */
	public AbstractServiceInstance(ServiceType serviceType, DateTime start, DateTime end) {
		super(serviceType);
		this.serviceType = Objects.requireNonNull(serviceType, "serviceType");
		Objects.requireNonNull(start,"start"); 
		Objects.requireNonNull(end,"end");
		
   		 if (start.isAfter(end))
            throw new IllegalArgumentException("A serviceInstance cannot end before it starts.");

		 if (start.isBefore(serviceType.getStartOfPeriodOfOperation()))
	            throw new IllegalArgumentException("A serviceInstance cannot begin before the period of serviceType has begun.");

		 if (end.isAfter(serviceType.getEndOfPeriodOfOperation()))
			 	throw new IllegalArgumentException("A serviceType cannot end after the period of serviceType was finished.");

		this.scheduledStart = Objects.requireNonNull(start,"start");
		this.scheduledEnd = Objects.requireNonNull(end,"end");
		this.serviceDeliveryStatus = ServiceDeliveryStatus.SCHEDULED;
	}
	
	@Override
	public DateTime getStart(){
		return this.scheduledStart;
	}
	
	@Override
	public DateTime getEnd(){
		return this.scheduledEnd;
	}
	
	@Override
	public ServiceDeliveryStatus getServiceDeliveryStatus(){
		if (this.serviceDeliveryStatus == ServiceDeliveryStatus.CANCELLED){}
		else{
				if(Shop.INSTANCE.getTime().getDateTime().isBefore(scheduledStart)){
					this.serviceDeliveryStatus = ServiceDeliveryStatus.SCHEDULED;
				}
				else {	if(Shop.INSTANCE.getTime().getDateTime().isAfter(scheduledEnd)){
							this.serviceDeliveryStatus = ServiceDeliveryStatus.COMPLETED;
							}
				
						else{this.serviceDeliveryStatus = ServiceDeliveryStatus.EXECUTING;
							}
				}
		}
		return this.serviceDeliveryStatus;
	}
	
	public ServiceDeliveryStatus getServiceDeliveryStatusOnTime(DateTime dateTime){
		if (this.serviceDeliveryStatus == ServiceDeliveryStatus.CANCELLED){}
		else{
				if(dateTime.isBefore(scheduledStart)){
					this.serviceDeliveryStatus = ServiceDeliveryStatus.SCHEDULED;
				}
				else {	if(dateTime.isAfter(scheduledEnd)){
							this.serviceDeliveryStatus = ServiceDeliveryStatus.COMPLETED;
							}
				
						else{this.serviceDeliveryStatus = ServiceDeliveryStatus.EXECUTING;
							}
				}
		}
		return this.serviceDeliveryStatus;
	}
	
	public void cancelServiceInstance(){
		this.scheduledEnd = Shop.INSTANCE.getTime().getDateTime();
		this.serviceDeliveryStatus = ServiceDeliveryStatus.CANCELLED;
	}
	
	public ServiceType getServiceType(){
		return this.serviceType;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(other == this) return true;
		if(!(other instanceof ServiceInstance)) return false;
		return this.equals((ServiceInstance)other);
	}
	
	/**
	 * Determines if the given {@link ServiceInstance} is equal to this one or
	 * not. Two ServiceInstances are equal to each other, if their hash code is
	 * the same.
	 * 
	 * @param other
	 *            this one should be compared with
	 * @return <code>true</code> if and only if the hashCode of this Object
	 *         equals the hashCode of the object given as parameter.
	 *         <code>false</code> otherwise.
	 */
	
	public boolean equals(ServiceInstance other) {
		if(other == null) return false;
		if(other == this) return true;
		return this.getProductIdentifier().equals(other.getSerialNumber());
	}
	
	/**
	 * Returns the hash code for this entry. The hash of this object is the hash
	 * of its primary key.
	 * 
	 * @return the hash code for this entry
	 */
	
	@Override
	public int hashCode() {
		return this.getProductIdentifier().hashCode();
	}

}
