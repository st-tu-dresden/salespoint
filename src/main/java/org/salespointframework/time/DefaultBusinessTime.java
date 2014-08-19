package org.salespointframework.time;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

/**
 * A mutable implementation of {@link BusinessTime} to record {@link Duration}s to calculate the current business time
 * by accumulating them.
 * 
 * @author Oliver Gierke
 */
@Service
class DefaultBusinessTime implements BusinessTime {

	private Duration duration = Duration.ZERO;

	/* 
	 * (non-Javadoc)
	 * @see org.salespointframework.time.BusinessTime#getTime()
	 */
	@Override
	public LocalDateTime getTime() {
		return LocalDateTime.now().plus(duration);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.salespointframework.time.BusinessTime#forward(java.time.Duration)
	 */
	@Override
	public void forward(Duration duration) {
		this.duration = this.duration.plus(duration);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.salespointframework.time.BusinessTime#getOffset()
	 */
	@Override
	public Duration getOffset() {
		return this.duration;
	}
}
