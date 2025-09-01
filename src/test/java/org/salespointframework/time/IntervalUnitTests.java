/*
 * Copyright 2017-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.salespointframework.time;

import static org.assertj.core.api.Assertions.*;

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

		var start = LocalDateTime.now();
		var end = start.plusDays(2);

		var first = Interval.from(start).to(end);
		var second = Interval.from(start).to(end);

		assertThat(first).isEqualTo(first);
		assertThat(first).isEqualTo(second);
		assertThat(second).isEqualTo(first);
	}

	@Test // #8
	void instancesWithDifferentEndsAreNotEqual() {

		var start = LocalDateTime.now();

		var first = Interval.from(start).to(start.plusDays(1));
		var second = Interval.from(start).to(start.plusDays(2));

		assertThat(first).isNotEqualTo(second);
		assertThat(second).isNotEqualTo(first);
	}

	@Test // #8
	void instancesWithDifferentStartsAreNotEqual() {

		var reference = LocalDateTime.now();

		var first = Interval.from(reference.minusDays(1)).to(reference);
		var second = Interval.from(reference.minusDays(2)).to(reference);

		assertThat(first).isNotEqualTo(second);
		assertThat(second).isNotEqualTo(first);
	}

	@Test // #8
	void instancesWithDifferentStartsAndEndsAreNotEqual() {

		var reference = LocalDateTime.now();

		var first = Interval.from(reference.minusDays(1)).to(reference);
		var second = Interval.from(reference.plusDays(1)).to(reference.plusDays(2));

		assertThat(first).isNotEqualTo(second);
		assertThat(second).isNotEqualTo(first);
	}

	@Test // #153
	void detectsContainedDateTimes() {

		var now = LocalDateTime.now();
		var nowTomorrow = now.plusDays(1);
		var nowYesterday = now.minusDays(1);

		var interval = Interval.from(nowYesterday).to(nowTomorrow);

		assertThat(interval.contains(now)).isTrue();
		assertThat(interval.contains(nowTomorrow)).isTrue();
		assertThat(interval.contains(nowYesterday)).isTrue();

		assertThat(interval.contains(nowYesterday.minusDays(1))).isFalse();
		assertThat(interval.contains(nowTomorrow.plusDays(1))).isFalse();
	}

	@Test // #153
	void rejectsNullForContainsReference() {

		var now = LocalDateTime.now();

		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> Interval.from(now).to(now.plusDays(1)).contains(null));
	}

	@Test // #153
	void detectsOverlaps() {

		var now = LocalDateTime.now();

		var first = Interval.from(now.minusDays(1)).to(now.plusDays(2));
		var second = Interval.from(now.minusDays(2)).to(now.plusDays(1));
		var third = Interval.from(now).to(now.plusDays(1));
		var fourth = Interval.from(now.minusDays(2)).to(now.minusDays(1));

		// Partial
		assertThat(first.overlaps(second)).isTrue();
		assertThat(second.overlaps(first)).isTrue();

		// Containment
		assertThat(third.overlaps(first)).isTrue();
		assertThat(first.overlaps(third)).isTrue();

		// No overlap
		assertThat(first.overlaps(fourth)).isFalse();
		assertThat(fourth.overlaps(first)).isFalse();
	}

	@Test // #153
	void rejectsNullForOverlapReference() {

		var now = LocalDateTime.now();

		assertThatExceptionOfType(IllegalArgumentException.class) //
				.isThrownBy(() -> Interval.from(now).to(now.plusDays(1)).overlaps(null));
	}

	@Test // #162
	void exposesIntervalAsDuration() {

		var now = LocalDateTime.now();

		assertThat(Interval.from(now).to(now.plusDays(1)).toDuration()).isEqualTo(Duration.ofDays(1));
	}
}
