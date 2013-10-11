package org.salespointframework.core.time;

import org.joda.time.DateTime;
import org.joda.time.Duration;


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

	/**
	 * 
	 * @param duration
	 * @throws NullPointerException if duration is null
	 */
	void forward(Duration duration);
}
