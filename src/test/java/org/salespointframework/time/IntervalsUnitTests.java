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
}
