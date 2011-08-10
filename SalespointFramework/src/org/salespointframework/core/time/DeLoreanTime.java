package org.salespointframework.core.time;


import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.salespointframework.util.Objects;
import org.salespointframework.util.Observable;
import org.salespointframework.util.Observer;

//TODO OBSERVER
public class DeLoreanTime implements Time /*, Observable<Duration> */ {

	Duration duration = Duration.ZERO;

	@Override
	public final DateTime getDateTime() {
		return new DateTime().plus(duration);
	}

	@Override
	public final void goAhead(Duration duration) {
		Objects.requireNonNull(duration, "duration");
		beforeGoAhead(duration);
		this.duration = this.duration.plus(duration);
		afterGoAhead(duration);
	}
	
	//Hook
	public void beforeGoAhead(Duration duration) {
	
	}
	
	//Hook
	public void afterGoAhead(Duration duration) {
		
	}


}
