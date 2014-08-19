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

		LocalDateTime start = LocalDateTime.now();
		LocalDateTime end = start.plusDays(10);

		Intervals intervals = new Intervals(start, end, Duration.ofDays(2));

		assertThat(intervals, is(iterableWithSize(5)));
	}

	@Test
	public void setsUpExceedingIntervalsCorrectly() {

		LocalDateTime start = LocalDateTime.now();
		LocalDateTime end = start.plusDays(10);

		Intervals intervals = new Intervals(start, end, Duration.ofDays(3));

		assertThat(intervals, is(iterableWithSize(4)));
	}
}
