/*
 * Copyright 2019-2023 the original author or authors.
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
package org.salespointframework.order;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.salespointframework.order.OrderEvents.OrderCanceled;
import org.salespointframework.order.OrderEvents.OrderCompleted;
import org.salespointframework.useraccount.UserAccount.UserAccountIdentifier;
import org.springframework.modulith.test.AggregateTestUtils;
import org.springframework.modulith.test.PublishedEvents;
import org.springframework.modulith.test.PublishedEventsExtension;

/**
 * Unit tests for {@link Order}.
 *
 * @author Oliver Drotbohm
 * @since 7.3
 */
@ExtendWith(PublishedEventsExtension.class)
class OrderUnitTests {

	@Test
	public void publishesEventsForStateTransitions() {

		Order canceled = new Order(UserAccountIdentifier.of("4711")) //
				.markPaid() //
				.complete() //
				.cancel("No reason");

		PublishedEvents events = AggregateTestUtils.eventsOf(canceled);

		assertThat(events.ofType(OrderCompleted.class)).hasSize(1);
		assertThat(events.ofType(OrderCanceled.class)).hasSize(1);
	}
}
