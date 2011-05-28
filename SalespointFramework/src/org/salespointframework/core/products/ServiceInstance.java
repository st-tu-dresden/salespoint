package org.salespointframework.core.products;

public abstract class ServiceInstance<T extends ServiceType> extends ProductInstance<T> {
	public abstract ServiceDeliveryStatus getServiceDeliveryStatus();
	
	public ServiceInstance(T productType) {
		super(productType);
	}
	
}
