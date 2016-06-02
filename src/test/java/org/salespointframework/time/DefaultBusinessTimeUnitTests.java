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
package org.salespointframework.time;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.stream.IntStream;

import org.junit.Test;

/**
 * Unit tests for {@link DefaultBusinessTime}.
 * 
 * @author Oliver Gierke
 */
public class DefaultBusinessTimeUnitTests {

	BusinessTime businessTime = new DefaultBusinessTime();

	/**
	 * @see #7
	 */
	@Test
	public void returnsCurrentTimeByDefault() throws Exception {

		LocalDateTime time = businessTime.getTime();

		Thread.sleep(20);

		assertThat(time.isBefore(LocalDateTime.now()), is(true));
		assertThat(time.isAfter(LocalDateTime.now().minusSeconds(1)), is(true));
	}

	/**
	 * @see #7
	 */
	@Test
	public void shiftsTimeByGivenDuration() throws Exception {
		assertTimeShifted(businessTime.getTime(), 2);
	}

	/**
	 * @see #7
	 */
	@Test
	public void forwardingMultipleTimesAccumulatesOffset() throws Exception {
		assertTimeShifted(businessTime.getTime(), 2, 4, 12);
	}

	private void assertTimeShifted(LocalDateTime reference, int... days) throws Exception {

		int total = IntStream.of(days).//
				peek(day -> businessTime.forward(Duration.ofDays(day))).//
				sum();

		Thread.sleep(20);

		LocalDateTime time = businessTime.getTime();

		assertThat(time.isAfter(reference.plusDays(total)), is(true));
		assertThat(time.isBefore(reference.plusDays(total).plusSeconds(1)), is(true));
		assertThat(businessTime.getOffset(), is(Duration.ofDays(total)));
	}
}
