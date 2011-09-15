package org.salespointframework.core.time;

import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * 
 * @author Paul Henke
 * 
 */
public interface Time
{
	DateTime getDateTime();
	void goAhead(Duration duration); // TODO besserer Name? oder überhaupt auf
										// dem Interface?
}
