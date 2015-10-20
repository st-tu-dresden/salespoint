package org.salespointframework.time;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

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
	private Interval(LocalDateTime start, LocalDateTime end) {

		Assert.notNull(start, "Start must not be null!");
		Assert.notNull(end, "End must not be null!");
		Assert.isTrue(start.isBefore(end) || start.isEqual(end), "Start must be before or equal to end!");

		this.start = start;
		this.end = end;
	}

	/**
	 * Starts building a new {@link Interval} with the given start time.
	 * 
	 * @param start must not be {@literal null}.
	 * @return
	 */
	public static IntervalBuilder from(LocalDateTime start) {
		return new IntervalBuilder(start);
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

	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public static class IntervalBuilder {

		private final @NonNull LocalDateTime from;

		/**
		 * Creates an {@link Interval} from the current start time until the given end time.
		 * 
		 * @param end must not be {@literal null} and after the current start time or equal to it.
		 * @return
		 */
		public Interval to(LocalDateTime end) {
			return new Interval(from, end);
		}
	}
}
