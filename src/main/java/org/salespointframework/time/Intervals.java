package org.salespointframework.time;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.util.Assert;

/**
 * Value object to represent a list o {@link Interval}s.
 * 
 * @author Oliver Gierke
 */
public class Intervals implements Iterable<Interval> {

	private final List<Interval> intervals;

	/**
	 * Creates a new {@link Intervals} instance with all {@link Interval}s of the given duration between the givne start
	 * and end date.
	 * 
	 * @param start must not be {@literal null}.
	 * @param end must not be {@literal null}.
	 * @param duration must not be {@literal null}.
	 */
	public Intervals(LocalDateTime start, LocalDateTime end, Duration duration) {

		Assert.notNull(start, "Start date must not be null!");
		Assert.notNull(end, "End date must not be null!");
		Assert.notNull(duration, "Duration must not be null!");
		Assert.isTrue(!duration.isNegative(), "Duration must not be negative");

		this.intervals = getIntervals(start, end, duration);
	}

	/**
	 * Recursively builds up all {@link Interval}s of the given duration between the given start and end date.
	 * 
	 * @param start must not be {@literal null}.
	 * @param end must not be {@literal null}.
	 * @param duration must not be {@literal null}.
	 * @return
	 */
	private static List<Interval> getIntervals(LocalDateTime start, LocalDateTime end, Duration duration) {

		LocalDateTime target = start.plus(duration);

		if (!target.isBefore(end)) {
			return Collections.singletonList(Interval.from(start).to(end));
		}

		List<Interval> intervals = new ArrayList<>();
		intervals.add(Interval.from(start).to(target));
		intervals.addAll(getIntervals(target, end, duration));

		return Collections.unmodifiableList(intervals);
	}

	/**
	 * Returns a {@link Stream} of {@link Interval}s.
	 * 
	 * @return
	 */
	public Stream<Interval> stream() {
		return intervals.stream();
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
