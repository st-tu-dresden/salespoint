package org.salespointframework.time;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.util.Assert;

/**
 * Simple value object to represent time intervals.
 * 
 * @author Oliver Gierke
 */
public final class Interval {

	private final LocalDateTime start;
	private final LocalDateTime end;

	/**
	 * Creates a new {@link Interval} between the given start and end.
	 * 
	 * @param start must not be {@literal null}.
	 * @param end must not be {@literal null}.
	 */
	public Interval(LocalDateTime start, LocalDateTime end) {

		Assert.notNull(start, "Start must not be null!");
		Assert.notNull(end, "End must not be null!");

		this.start = start;
		this.end = end;
	}

	/**
	 * Returns the start date of the {@link Interval}.
	 * 
	 * @return the start
	 */
	public LocalDateTime getStart() {
		return start;
	}

	/**
	 * Returns the end date of the {@link Interval}.
	 * 
	 * @return the end
	 */
	public LocalDateTime getEnd() {
		return end;
	}

	/**
	 * Returns the duration of the interval.
	 * 
	 * @return
	 */
	public Duration getDuration() {
		return Duration.between(start, end);
	}

	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Interval)) {
			return false;
		}

		Interval that = (Interval) obj;

		return this.start.equals(that.start) && this.end.equals(that.end);
	}

	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(start, end);
	}

	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("Interval from %s to %s", start, end);
	}
}
