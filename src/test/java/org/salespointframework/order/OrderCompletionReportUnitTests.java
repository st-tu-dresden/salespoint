package org.salespointframework.order;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.salespointframework.order.OrderCompletionReport.OrderLineCompletion;

/**
 * Unit tests for {@link OrderCompletionReport}.
 * 
 * @author Oliver Gierke
 */
public class OrderCompletionReportUnitTests {

	/**
	 * @see #144
	 */
	@Test
	public void erroneousCompletionExposesFailure() {

		OrderLineCompletion completion = OrderLineCompletion.error(mock(OrderLine.class), "Some message!");

		assertThat(completion.isFailure()).isTrue();
		assertThat(completion.getMessage()).hasValueSatisfying(it -> {
			assertThat(it).isEqualTo("Some message!");
		});
	}
}
