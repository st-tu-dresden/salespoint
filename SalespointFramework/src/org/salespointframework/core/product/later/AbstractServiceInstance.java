package org.salespointframework.core.product.later;

import org.joda.time.DateTime;


public abstract class AbstractServiceInstance<T extends ServiceType> implements ServiceInstance<T> {
	
	private DateTime start;
	private DateTime end;
	private ServiceDeliveryStatus serviceDeliveryStatus;
	
	public AbstractServiceInstance(T productType) {

	}
	
	public DateTime getStart(){
		return this.start;
	}
	public DateTime getEnd(){
		return this.end;
	}
	public ServiceDeliveryStatus getServiceDeliveryStatus(){
		return this.serviceDeliveryStatus;
		}
	




}
