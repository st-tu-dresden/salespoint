package org.salespointframework.core.product.later;

import org.joda.time.DateTime;
import org.salespointframework.core.product.ProductInstance;

public interface ServiceInstance extends ProductInstance {

	ServiceDeliveryStatus getServiceDeliveryStatus();
	DateTime getStart();
	DateTime getEnd();
	ServiceType getServiceType();
	void cancelServiceInstance();
}
