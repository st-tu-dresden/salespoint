package org.salespointframework.core.product.service;

import org.joda.time.DateTime;
import org.salespointframework.core.product.Product;

/**
 * This is an interface which provides basic methods to handle ServiceTypes.
 * 
 */

public interface Service extends Product
{
	/**
	 * Returns the Start of the ServiceType
	 * 
	 * @return the start of the ServiceType
	 */

	DateTime getStartOfPeriodOfOperation();

	/**
	 * Returns the End of the ServiceType
	 * 
	 * @return the end of the ServiceType
	 */

	DateTime getEndOfPeriodOfOperation();
}
