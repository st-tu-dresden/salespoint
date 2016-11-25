package org.salespointframework.time;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.Test;

/**
 * Unit tests for {@link Intervals}.
 *
 * @author Oliver Gierke
 */
public class IntervalsUnitTests {

	@Test
	public void setsUpIntervalsCorrectly() {

		Interval interval = Interval.from(LocalDateTime.now()).withLength(Duration.ofDays(10));
		Intervals intervals = Intervals.divide(interval, Duration.ofDays(2));

		assertThat(intervals, is(iterableWithSize(5)));
	}

	@Test
	public void setsUpExceedingIntervalsCorrectly() {

		Interval interval = Interval.from(LocalDateTime.now()).withLength(Duration.ofDays(10));
		Intervals intervals = Intervals.divide(interval, Duration.ofDays(3));

		assertThat(intervals, is(iterableWithSize(4)));
	}

	/**
	 * @see #153
	 */
	@Test
	public void detectsContainedDateTimes() {

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime nowTomorrow = now.plusDays(1);
		LocalDateTime nowYesterday = now.minusDays(1);

		Interval interval = Interval.from(nowYesterday).to(nowTomorrow);

		assertThat(interval.contains(now), is(true));
		assertThat(interval.contains(nowTomorrow), is(true));
		assertThat(interval.contains(nowYesterday), is(true));

		assertThat(interval.contains(nowYesterday.minusDays(1)), is(false));
		assertThat(interval.contains(nowTomorrow.plusDays(1)), is(false));
	}

	/**
	 * @see #153
	 */
	@Test(expected = IllegalArgumentException.class)
	public void rejectsNullForContainsReference() {

		LocalDateTime now = LocalDateTime.now();

		Interval.from(now).to(now.plusDays(1)).contains(null);
	}

	/**
	 * @see #153
	 */
	@Test
	public void detectsOverlaps() {

		LocalDateTime now = LocalDateTime.now();

		Interval first = Interval.from(now.minusDays(1)).to(now.plusDays(2));
		Interval second = Interval.from(now.minusDays(2)).to(now.plusDays(1));
		Interval third = Interval.from(now).to(now.plusDays(1));
		Interval fourth = Interval.from(now.minusDays(2)).to(now.minusDays(1));

		// Partial
		assertThat(first.overlaps(second), is(true));
		assertThat(second.overlaps(first), is(true));

		// Containment
		assertThat(third.overlaps(first), is(true));
		assertThat(first.overlaps(third), is(true));

		// No overlap
		assertThat(first.overlaps(fourth), is(false));
		assertThat(fourth.overlaps(first), is(false));
	}

	/**
	 * @see #153
	 */
	@Test(expected = IllegalArgumentException.class)
	public void rejectsNullForOverlapReference() {

		LocalDateTime now = LocalDateTime.now();

		Interval.from(now).to(now.plusDays(1)).overlaps(null);
	}
}
