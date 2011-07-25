package org.salespointframework.core.product.later;

import org.joda.time.DateTime;
import org.salespointframework.core.product.ProductInstance;

public interface ServiceInstance<T extends ServiceType> extends ProductInstance {

	ServiceDeliveryStatus getServiceDeliveryStatus();
	DateTime getStart();
	DateTime getEnd();
	DateTime getScheduledStart();
	DateTime getScheduledEnd();
	T getServiceType();
	void cancelServiceInstance();
}
