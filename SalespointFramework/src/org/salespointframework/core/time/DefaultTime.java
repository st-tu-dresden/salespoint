package org.salespointframework.core.time;

import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * TODO
 * @author Paul Henke
 * 
 */
public class DefaultTime implements Time
{
	@Override
	public DateTime getDateTime()
	{
		return new DateTime();
	}

	/**
	 * this method does nothing
	 */
	@Override
	public void forward(Duration duration)
	{

	}
}
