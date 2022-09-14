/*
 * Copyright 2017-2022 the original author or authors.
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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;

import org.springframework.util.Assert;

/**
 * Simple value object to represent time intervals. Note that whether the endpoints are included
 * or not can vary between the offered methods.
 * 
 * @author Oliver Drotbohm
 * @author Martin Morgenstern
 */
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public final class Interval {

	/**
	 * The start date of the {@link Interval}.
	 */
	LocalDateTime start;

	/**
	 * The end date of the {@link Interval}.
	 */
	LocalDateTime end;

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
	 * @return will never be {@literal null}.
	 */
	public static IntervalBuilder from(LocalDateTime start) {
		return new IntervalBuilder(start);
	}

	/**
	 * Returns the duration of the interval, with the end excluded.
	 * 
	 * @return will never be {@literal null}.
	 */
	public Duration getDuration() {
		return Duration.between(start, end);
	}

	/**
	 * Returns whether the given {@link LocalDateTime} is contained in the current {@link Interval}.
	 * The comparison includes start and end, i.e., the method treats this interval as closed.
	 * 
	 * @param reference must not be {@literal null}.
	 * @return
	 */
	public boolean contains(LocalDateTime reference) {

		Assert.notNull(reference, "Reference must not be null!");

		boolean afterOrOnStart = start.isBefore(reference) || start.isEqual(reference);
		boolean beforeOrOnEnd = end.isAfter(reference) || end.isEqual(reference);

		return afterOrOnStart && beforeOrOnEnd;
	}

	/**
	 * Returns whether the current {@link Interval} overlaps with the given one. The
	 * comparison excludes start and end, i.e., the method treats both intervals as open.
	 * 
	 * @param reference must not be {@literal null}.
	 * @return
	 */
	public boolean overlaps(Interval reference) {

		Assert.notNull(reference, "Reference must not be null!");

		boolean endsAfterOtherStarts = getEnd().isAfter(reference.getStart());
		boolean startsBeforeOtherEnds = getStart().isBefore(reference.getEnd());

		return startsBeforeOtherEnds && endsAfterOtherStarts;
	}

	/**
	 * Returns the {@link Duration} represented by the given {@link Interval}, with the
	 * end excluded.
	 * 
	 * @return
	 */
	public Duration toDuration() {
		return Duration.between(start, end);
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
		 * @return will never be {@literal null}.
		 */
		public Interval to(LocalDateTime end) {
			return new Interval(from, end);
		}

		/**
		 * Creates a new {@link Interval} from the current start time adding the given {@link TemporalAmount} to it.
		 * 
		 * @param amount must not be {@literal null}.
		 * @return will never be {@literal null}.
		 * @see Duration
		 */
		public Interval withLength(TemporalAmount amount) {

			Assert.notNull(amount, "Temporal amount must not be null!");
			return new Interval(from, from.plus(amount));
		}
	}
}
