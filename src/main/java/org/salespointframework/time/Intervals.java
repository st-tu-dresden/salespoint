package org.salespointframework.time;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.salespointframework.core.Streamable;
import org.springframework.util.Assert;

/**
 * Value object to represent a list o {@link Interval}s.
 * 
 * @author Oliver Gierke
 */
public class Intervals implements Streamable<Interval> {

	private final Iterable<Interval> intervals;

	/**
	 * Creates a new {@link Intervals} instance with all {@link Interval}s of the given duration between the givne start
	 * and end date.
	 * 
	 * @param start must not be {@literal null}.
	 * @param end must not be {@literal null}.
	 * @param duration must not be {@literal null}.
	 */
	private Intervals(LocalDateTime start, LocalDateTime end, Duration duration) {

		Assert.notNull(start, "Start date must not be null!");
		Assert.notNull(end, "End date must not be null!");
		Assert.notNull(duration, "Duration must not be null!");
		Assert.isTrue(!duration.isNegative(), "Duration must not be negative");

		this.intervals = getIntervals(start, end, duration);
	}

	/**
	 * Divides the given {@link Interval} into smaller intervals of the given duration.
	 * 
	 * @param interval must not be {@literal null}.
	 * @param duration must not be {@literal null}.
	 * @return
	 */
	public static Intervals divide(Interval interval, Duration duration) {

		Assert.notNull(interval, "Interval must not be null!");
		Assert.notNull(duration, "Duration must not be null!");

		return new Intervals(interval.getStart(), interval.getEnd(), duration);
	}

	/**
	 * Recursively builds up all {@link Interval}s of the given duration between the given start and end date.
	 * 
	 * @param start must not be {@literal null}.
	 * @param end must not be {@literal null}.
	 * @param duration must not be {@literal null}.
	 * @return
	 */
	private static Collection<Interval> getIntervals(LocalDateTime start, LocalDateTime end, Duration duration) {

		LocalDateTime target = start.plus(duration);

		if (!target.isBefore(end)) {
			return Collections.singleton(Interval.from(start).to(end));
		}

		List<Interval> intervals = new ArrayList<>();
		intervals.add(Interval.from(start).to(target));
		intervals.addAll(getIntervals(target, end, duration));

		return Collections.unmodifiableList(intervals);
	}

	/* 
	 * (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Interval> iterator() {
		return intervals.iterator();
	}
}
