package org.salespointframework.core.product.later;

import org.salespointframework.core.product.ProductInstance;

public interface ServiceInstance<T extends ServiceType> extends ProductInstance<T> {

	ServiceDeliveryStatus getServiceDeliveryStatus();
}
