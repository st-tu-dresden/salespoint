package org.salespointframework.core.time;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.salespointframework.util.ArgumentNullException;
import org.salespointframework.util.Objects;

// TODO comment
/**
 * 
 * @author Paul Henke
 *
 */
public class DeLoreanTime implements Time
{
	Duration duration = Duration.ZERO;

	@Override
	public final DateTime getDateTime()
	{
		return new DateTime().plus(duration);
	}

	/**
	 * you need 1.21 gigawatts for this method
	 */
	@Override
	public final void forward(Duration duration)
	{
		Objects.requireNonNull(duration, "duration");
		beforeForward(duration);
		this.duration = this.duration.plus(duration);
		afterForward(duration);
	}

	/**
	 * Hook method, is called before forward is called
	 * @param duration
	 */
	public void beforeForward(Duration duration)
	{

	}

	/**
	 * Hook method, is called after forward was called
	 * @param duration
	 */
	public void afterForward(Duration duration)
	{

	}
}
