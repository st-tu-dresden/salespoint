package org.salespointframework.core.product.later;

import org.joda.time.DateTime;
import org.salespointframework.core.product.ProductType;

/**
 * This is an interface which provides basic methods to handle ServiceTypes.
 * 
 */

public interface ServiceType extends ProductType
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
