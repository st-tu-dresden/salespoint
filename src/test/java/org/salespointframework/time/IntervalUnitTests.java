package org.salespointframework.time;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;

/**
 * Unit tests for {@link Interval}.
 * 
 * @author Oliver Gierke
 */
public class IntervalUnitTests {

	/**
	 * @see #8
	 */
	@Test(expected = IllegalArgumentException.class)
	public void preventsNullStart() {
		Interval.from(null);
	}

	/**
	 * @see #8
	 */
	@Test(expected = IllegalArgumentException.class)
	public void preventsNullEnd() {
		Interval.from(LocalDateTime.now()).to(null);
	}

	/**
	 * @see #8
	 */
	@Test
	public void instancesWithSameStartAndEndAreEqual() {

		LocalDateTime start = LocalDateTime.now();
		LocalDateTime end = start.plusDays(2);

		Interval first = Interval.from(start).to(end);
		Interval second = Interval.from(start).to(end);

		assertThat(first, is(first));
		assertThat(first, is(second));
		assertThat(second, is(first));
	}

	/**
	 * @see #8
	 */
	@Test
	public void instancesWithDifferentEndsAreNotEqual() {

		LocalDateTime start = LocalDateTime.now();

		Interval first = Interval.from(start).to(start.plusDays(1));
		Interval second = Interval.from(start).to(start.plusDays(2));

		assertThat(first, is(not(second)));
		assertThat(second, is(not(first)));
	}

	/**
	 * @see #8
	 */
	@Test
	public void instancesWithDifferentStartsAreNotEqual() {

		LocalDateTime reference = LocalDateTime.now();

		Interval first = Interval.from(reference.minusDays(1)).to(reference);
		Interval second = Interval.from(reference.minusDays(2)).to(reference);

		assertThat(first, is(not(second)));
		assertThat(second, is(not(first)));
	}

	/**
	 * @see #8
	 */
	@Test
	public void instancesWithDifferentStartsAndEndsAreNotEqual() {

		LocalDateTime reference = LocalDateTime.now();

		Interval first = Interval.from(reference.minusDays(1)).to(reference);
		Interval second = Interval.from(reference.plusDays(1)).to(reference.plusDays(2));

		assertThat(first, is(not(second)));
		assertThat(second, is(not(first)));
	}
}
