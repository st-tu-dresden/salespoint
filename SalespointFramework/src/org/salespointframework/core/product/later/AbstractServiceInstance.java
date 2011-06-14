package org.salespointframework.core.product.later;


public abstract class AbstractServiceInstance<T extends ServiceType> implements ServiceInstance<T> {
	public abstract ServiceDeliveryStatus getServiceDeliveryStatus();
	
	public AbstractServiceInstance(T productType) {

	}
	
}
