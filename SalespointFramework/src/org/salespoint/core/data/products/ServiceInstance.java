package org.salespoint.core.data.products;

public abstract class ServiceInstance<T extends ServiceType> extends ProductInstance<T> {
	public abstract ServiceDeliveryStatus getServiceDeliveryStatus();
}
