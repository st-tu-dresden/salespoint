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

import lombok.Value;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

import org.jmolecules.event.types.DomainEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Component to allow access to the current business time. It will usually return the current system time but allows
 * manually forwarding it by a certain {@link Duration} for testing and simulation purposes.
 *
 * @author Paul Henke
 * @author Oliver Gierke
 * @author Rico Bergmann
 */
@Service
public interface BusinessTime {

	/**
	 * Returns the current business time. This will be the time of the system the application is running on by default but
	 * can be adjusted by calling {@link #forward(Duration)}.
	 *
	 * @return
	 */
	LocalDateTime getTime();

	/**
	 * Forwards the current time with the given {@link Duration}. Calling the method multiple times will accumulate
	 * durations. For each day passed during the foward a {@link DayHasPassed} event will be published for application
	 * components to react to (e.g. via {@link EventListener}). Also, for each month passed, a {@link MonthHasPassed}
	 * event will be published the same way.
	 *
	 * @param duration must not be {@literal null}.
	 */
	void forward(Duration duration);

	/**
	 * Returns the current offset between the real time and the virtual one created by calling {@link #forward(Duration)}.
	 *
	 * @return
	 */
	Duration getOffset();

	/**
	 * Undoes any forwarding. Afterwards any call to {@link #getTime()} will be equivalent to the system's time again.
	 */
	void reset();

	/**
	 * A {@link DomainEvent} published on each day. Implement an {@link EventListener} to trigger functionality that has
	 * to run each day.
	 *
	 * @author Oliver Drotbohm
	 * @since 7.5
	 */
	@Value(staticConstructor = "of")
	public class DayHasPassed implements DomainEvent {

		/**
		 * The day that has just passed.
		 */
		private final LocalDate date;
	}

	/**
	 * A {@link DomainEvent} published on the last day of the month. Implement an {@link EventListener} to trigger
	 * functionality that has to run each month.
	 *
	 * @author Oliver Drotbohm
	 * @since 7.5
	 */
	@Value(staticConstructor = "of")
	public class MonthHasPassed implements DomainEvent {

		/**
		 * The month that has just passed.
		 */
		private final YearMonth month;
	}
}
