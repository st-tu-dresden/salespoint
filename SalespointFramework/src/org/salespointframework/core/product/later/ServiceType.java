package org.salespointframework.core.product.later;

import org.joda.time.DateTime;
import org.salespointframework.core.product.ProductType;

public interface ServiceType extends ProductType {

	DateTime getStartOfPeriodOfOperation();
	DateTime getEndOfPeriodOfOperation();
}
