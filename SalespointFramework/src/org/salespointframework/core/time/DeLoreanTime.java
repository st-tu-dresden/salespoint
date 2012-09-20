package org.salespointframework.core.time;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import java.util.Objects;

/**
 * A full {@link Time} implementation including forward().
 * Hook-methods are provided for convenience.
 * Use this implementation for testing.
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
		Objects.requireNonNull(duration, "duration must not be null");
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
