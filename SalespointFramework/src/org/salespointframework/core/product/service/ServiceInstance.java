package org.salespointframework.core.product.service;

import org.joda.time.DateTime;
import org.salespointframework.core.product.ProductInstance;

/**
 * This is an interface which provides basic methods to handle ServiceInstances.
 * 
 */

public interface ServiceInstance extends ProductInstance
{
	/**
	 * Returns the Status of the ServiceInstance
	 * 
	 * @return the {@link ServiceDeliveryStatus}
	 */

	ServiceDeliveryStatus getServiceDeliveryStatus();

	/**
	 * Returns the Status of the ServiceInstance at a specific time
	 * 
	 * @param dateTime
	 *            a specific time
	 * @return the {@link ServiceDeliveryStatus}
	 */

	ServiceDeliveryStatus getServiceDeliveryStatusOnTime(DateTime dateTime);

	/**
	 * Returns the Date of the Start of the ServiceInstance
	 * 
	 * @return the Start of the ServiceInstance
	 */

	DateTime getStart();

	/**
	 * Returns the Date of the End of the ServiceInstance
	 * 
	 * @return the End of the ServiceInstance
	 */

	DateTime getEnd();

	/**
	 * Returns the ServiceType of the ServiceInstance
	 * 
	 * @return the ServiceType of the ServiceInstance
	 */

	Service getServiceType();

	/**
	 * Cancels the ServiceInstance and so it will not be executed. The
	 * {@link ServiceDeliveryStatus} is "CANCELLED".
	 */

	void cancelServiceInstance();
}
