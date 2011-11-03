package org.salespointframework.core.time;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.salespointframework.util.ArgumentNullException;

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

	/**
	 * 
	 * @param duration
	 * @throws ArgumentNullException if duration is null
	 */
	void forward(Duration duration);
}
