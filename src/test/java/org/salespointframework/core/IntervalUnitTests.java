/*
 * Copyright 2014 the original author or authors.
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
package org.salespointframework.core;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;
import org.salespointframework.time.Interval;

/**
 * @author Oliver Gierke
 */
public class IntervalUnitTests {
	/**
	 * @see #8
	 */
	@Test(expected = NullPointerException.class)
	public void preventsNullStart() {
		new Interval(null, LocalDateTime.now());
	}

	/**
	 * @see #8
	 */
	@Test(expected = NullPointerException.class)
	public void preventsNullEnd() {
		new Interval(LocalDateTime.now(), null);
	}

	/**
	 * @see #8
	 */
	@Test
	public void instancesWithSameStartAndEndAreEqual() {

		LocalDateTime start = LocalDateTime.now();
		LocalDateTime end = start.plusDays(2);

		Interval first = new Interval(start, end);
		Interval second = new Interval(start, end);

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

		Interval first = new Interval(start, start.plusDays(1));
		Interval second = new Interval(start, start.plusDays(2));

		assertThat(first, is(not(second)));
		assertThat(second, is(not(first)));
	}

	/**
	 * @see #8
	 */
	@Test
	public void instancesWithDifferentStartsAreNotEqual() {

		LocalDateTime reference = LocalDateTime.now();

		Interval first = new Interval(reference.minusDays(1), reference);
		Interval second = new Interval(reference.minusDays(2), reference);

		assertThat(first, is(not(second)));
		assertThat(second, is(not(first)));
	}
	
	/**
	 * @see #8
	 */
	@Test
	public void instancesWithDifferentStartsAndEndsAreNotEqual() {

		LocalDateTime reference = LocalDateTime.now();

		Interval first = new Interval(reference.minusDays(1), reference);
		Interval second = new Interval(reference.plusDays(1), reference.plusDays(2));

		assertThat(first, is(not(second)));
		assertThat(second, is(not(first)));
	}
}
