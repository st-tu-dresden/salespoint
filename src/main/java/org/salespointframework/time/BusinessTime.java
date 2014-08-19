package org.salespointframework.time;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Component to allow access to the current business time. It will usually return the current system time but allows
 * manually forwarding it by a certain {@link Duration} for testing and simulation purposes.
 * 
 * @author Paul Henke
 * @author Oliver Gierke
 */
public interface BusinessTime {

	/**
	 * Returns the current business time. This will be the time of the system the application is running on by default but
	 * can be adjusted by calling {@link #forward(Duration)}.
	 * 
	 * @return
	 */
	LocalDateTime getTime();

	/**
	 * Forwards the current time with the given {@link Duration}. Calling the method multiple times will accumulate
	 * durations.
	 * 
	 * @param duration
	 */
	void forward(Duration duration);

	/**
	 * Returns the current offset between the real time and the virtual one created by calling {@link #forward(Duration)}.
	 * 
	 * @return
	 */
	Duration getOffset();
}
