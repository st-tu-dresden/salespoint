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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.salespointframework.time.BusinessTime.DayHasPassed;
import org.salespointframework.time.BusinessTime.MonthHasPassed;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Unit tests for {@link DefaultBusinessTime}.
 *
 * @author Oliver Gierke
 * @author Rico Bergmann
 */
@ExtendWith(MockitoExtension.class)
class DefaultBusinessTimeUnitTests {

	@InjectMocks DefaultBusinessTime businessTime;
	@Mock ApplicationEventPublisher events;

	@BeforeEach
	void reset() {
		businessTime.reset();
	}

	@Test // #7
	void returnsCurrentTimeByDefault() throws Exception {

		LocalDateTime time = businessTime.getTime();

		Thread.sleep(20);

		assertThat(time.isBefore(LocalDateTime.now())).isTrue();
		assertThat(time.isAfter(LocalDateTime.now().minusSeconds(1))).isTrue();
	}

	@Test // #7
	void shiftsTimeByGivenDuration() throws Exception {
		assertTimeShifted(businessTime.getTime(), 2);
	}

	@Test // #7
	void forwardingMultipleTimesAccumulatesOffset() throws Exception {
		assertTimeShifted(businessTime.getTime(), 2, 4, 12);
	}

	@Test // #189
	void resetsTime() throws Exception {

		businessTime.forward(Duration.ofDays(42));
		businessTime.reset();

		LocalDateTime businessNow = businessTime.getTime();
		Thread.sleep(20);
		LocalDateTime ldtNow = LocalDateTime.now();

		assertThat(ldtNow.isAfter(businessNow)).isTrue();
	}

	@Test // #189
	void resetsTimeDespiteMultipleForwards() throws Exception {

		businessTime.forward(Duration.ofDays(42));
		businessTime.forward(Duration.ofDays(13));
		businessTime.reset();

		LocalDateTime businessNow = businessTime.getTime();
		Thread.sleep(20);
		LocalDateTime ldtNow = LocalDateTime.now();

		assertThat(ldtNow.isAfter(businessNow)).isTrue();
	}

	@Test // #189
	void resetWithoutForwardDoesntBreakTime() throws Exception {

		LocalDateTime businessNow = businessTime.getTime();
		LocalDateTime now = LocalDateTime.now();

		assertThat(businessNow.isBefore(now.plusSeconds(1))).isTrue();
		assertThat(businessNow.plusSeconds(1).isAfter(now)).isTrue();
	}

	@Test // #189
	void forwardsCorrectlyAfterReset() throws Exception {

		assertTimeShifted(businessTime.getTime(), 42);
	}

	@Test // #371
	void emitsDayHasPassedEventOnForwarding() {

		businessTime.forward(Duration.ofDays(1));

		verify(events, times(1)).publishEvent(DayHasPassed.of(LocalDate.now()));
	}

	@Test // #371
	void emitsMonthHasPassedEventOnForwardingAcrossMonthBoundary() {

		var today = LocalDate.now();
		var lengthOfMonth = today.lengthOfMonth();

		businessTime.forward(Duration.ofDays(lengthOfMonth));

		verify(events, times(lengthOfMonth)).publishEvent(any(DayHasPassed.class));
		verify(events, times(1)).publishEvent(MonthHasPassed.of(YearMonth.from(today)));
	}

	private void assertTimeShifted(LocalDateTime reference, int... days) throws Exception {

		int total = IntStream.of(days).//
				peek(day -> businessTime.forward(Duration.ofDays(day))).//
				sum();

		Thread.sleep(20);

		LocalDateTime time = businessTime.getTime();

		assertThat(time.isAfter(reference.plusDays(total))).isTrue();
		assertThat(time.isBefore(reference.plusDays(total).plusSeconds(1))).isTrue();
		assertThat(businessTime.getOffset()).isEqualTo(Duration.ofDays(total));
	}
}
