package org.salespointframework.core.product.later;

import org.joda.time.DateTime;
import org.salespointframework.core.shop.Shop;
import org.salespointframework.core.time.ShopTime;
import org.salespointframework.util.Objects;


public abstract class AbstractServiceInstance<T extends ServiceType> implements ServiceInstance<T> {
	
	

	private T serviceType;
	
	private DateTime scheduledStart;
	private DateTime scheduledEnd;
	private ServiceDeliveryStatus serviceDeliveryStatus;
	
	@Deprecated
	public AbstractServiceInstance(){}
	
	public AbstractServiceInstance(T serviceType, DateTime start, DateTime end) {
		this.serviceType = Objects.requireNonNull(serviceType, "serviceType");
		this.scheduledStart = start;
		this.scheduledEnd = end;
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
	
	public void cancelServiceInstance(){
		this.scheduledEnd = Shop.INSTANCE.getTime().getDateTime();
		this.serviceDeliveryStatus = ServiceDeliveryStatus.CANCELLED;
	}
	
	public T getServiceType(){
		return this.serviceType;
	}

}
