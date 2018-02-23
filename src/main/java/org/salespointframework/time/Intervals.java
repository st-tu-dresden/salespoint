/*
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.salespointframework.time;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.springframework.data.util.Streamable;
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
	private Intervals(LocalDateTime start, LocalDateTime end, TemporalAmount duration) {

		Assert.notNull(start, "Start date must not be null!");
		Assert.notNull(end, "End date must not be null!");
		Assert.notNull(duration, "Duration must not be null!");

		this.intervals = getIntervals(start, end, duration);
	}

	/**
	 * Divides the given {@link Interval} into smaller intervals of the given duration.
	 * 
	 * @param interval must not be {@literal null}.
	 * @param duration must not be {@literal null}.
	 * @return
	 */
	public static Intervals divide(Interval interval, TemporalAmount duration) {

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
	private static Collection<Interval> getIntervals(LocalDateTime start, LocalDateTime end, TemporalAmount duration) {

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
