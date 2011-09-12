package org.salespointframework.core.time;

import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * 
 * @author Paul Henke
 *
 */
public class DefaultTime implements Time {
	@Override
	public DateTime getDateTime() {
		return new DateTime();
	}

	@Override
	public void goAhead(Duration duration) {
		// NO OP
	}
}
