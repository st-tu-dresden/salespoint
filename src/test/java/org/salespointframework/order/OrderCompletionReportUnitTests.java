/*
 * Copyright 2017-2022 the original author or authors.
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
package org.salespointframework.order;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.salespointframework.order.OrderCompletionReport.OrderLineCompletion;
import org.salespointframework.useraccount.UserAccount;

/**
 * Unit tests for {@link OrderCompletionReport}.
 * 
 * @author Oliver Gierke
 */
class OrderCompletionReportUnitTests {

	@Test // #144
	void erroneousCompletionExposesFailure() {

		OrderLineCompletion completion = OrderLineCompletion.error(mock(OrderLine.class), "Some message!");

		assertThat(completion.isFailure()).isTrue();
		assertThat(completion.getMessage()).hasValueSatisfying(it -> {
			assertThat(it).isEqualTo("Some message!");
		});
	}

	@Test // #215
	public void toStringRendersHumanReadableOutput() {

		UserAccount account = mock(UserAccount.class);
		Order order = new Order(account);

		OrderLineCompletion completion = OrderLineCompletion.error(mock(OrderLine.class), "Some message!");
		OrderCompletionReport report = OrderCompletionReport.forCompletions(order, Arrays.asList(completion));

		// toString() contains message of OrderLineCompletion
		assertThat(report.toString()).contains("Some message!");
	}
}
