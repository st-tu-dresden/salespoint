package org.salespointframework.core.products.later;


public abstract class AbstractServiceInstance<T extends ServiceType> implements ServiceInstance<T> {
	public abstract ServiceDeliveryStatus getServiceDeliveryStatus();
	
	public AbstractServiceInstance(T productType) {

	}
	
}
