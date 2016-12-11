package org.salespointframework.time;

import static org.hamcrest.Matchers.*;
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
