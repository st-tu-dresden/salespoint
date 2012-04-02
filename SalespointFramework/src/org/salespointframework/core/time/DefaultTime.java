package org.salespointframework.core.time;

import org.joda.time.DateTime;
import org.joda.time.Duration;


/**
 * A {@link Time} implementation, forward() is a no-op.
 * Use this implementation for production code.
 * 
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
