package org.salespointframework.core.time;

import org.joda.time.DateTime;
import org.joda.time.Duration;

// TODO comment
/**
 * 
 * @author Paul Henke
 * 
 */
public interface Time {
	/**
	 * 
	 * @return the current {@link DateTime} 
	 */
	DateTime getDateTime();

	void forward(Duration duration);
}
