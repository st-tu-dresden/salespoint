/*
 * Copyright 2017-2019 the original author or authors.
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

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.junit.MatcherAssert.assertThat;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Interval}.
 * 
 * @author Oliver Gierke
 */
class IntervalUnitTests {

	@Test // #8
	void preventsNullStart() {
		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> Interval.from(null));
	}

	@Test // #8
	void preventsNullEnd() {

		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> Interval.from(LocalDateTime.now()).to(null));
	}

	@Test // #8
	void instancesWithSameStartAndEndAreEqual() {

		LocalDateTime start = LocalDateTime.now();
		LocalDateTime end = start.plusDays(2);

		Interval first = Interval.from(start).to(end);
		Interval second = Interval.from(start).to(end);

		assertThat(first, is(first));
		assertThat(first, is(second));
		assertThat(second, is(first));
	}

	@Test // #8
	void instancesWithDifferentEndsAreNotEqual() {

		LocalDateTime start = LocalDateTime.now();

		Interval first = Interval.from(start).to(start.plusDays(1));
		Interval second = Interval.from(start).to(start.plusDays(2));

		assertThat(first).isNotEqualTo(second);
		assertThat(second).isNotEqualTo(first);
	}

	@Test // #8
	void instancesWithDifferentStartsAreNotEqual() {

		LocalDateTime reference = LocalDateTime.now();

		Interval first = Interval.from(reference.minusDays(1)).to(reference);
		Interval second = Interval.from(reference.minusDays(2)).to(reference);

		assertThat(first).isNotEqualTo(second);
		assertThat(second).isNotEqualTo(first);
	}

	@Test // #8
	void instancesWithDifferentStartsAndEndsAreNotEqual() {

		LocalDateTime reference = LocalDateTime.now();

		Interval first = Interval.from(reference.minusDays(1)).to(reference);
		Interval second = Interval.from(reference.plusDays(1)).to(reference.plusDays(2));

		assertThat(first).isNotEqualTo(second);
		assertThat(second).isNotEqualTo(first);
	}

	@Test // #153
	void detectsContainedDateTimes() {

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

	@Test // #153
	void rejectsNullForContainsReference() {

		LocalDateTime now = LocalDateTime.now();

		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> Interval.from(now).to(now.plusDays(1)).contains(null));
	}

	@Test // #153
	void detectsOverlaps() {

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

	@Test // #153
	void rejectsNullForOverlapReference() {

		LocalDateTime now = LocalDateTime.now();

		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> Interval.from(now).to(now.plusDays(1)).overlaps(null));
	}

	@Test // #162
	void exposesIntervalAsDuration() {

		LocalDateTime now = LocalDateTime.now();

		assertThat(Interval.from(now).to(now.plusDays(1)).toDuration(), is(Duration.ofDays(1)));
	}
}
