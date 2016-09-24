package org.salespointframework.inventory;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.salespointframework.order.OrderLine;

/**
 * Unit tests for {@link LineItemFilter}.
 * 
 * @author Oliver Gierke
 */
public class LineItemFilterUnitTests {

	List<LineItemFilter> filters;

	@Before
	public void setUp() {

		LineItemFilter first = item -> true;
		LineItemFilter second = item -> item.getProductName().startsWith("Trigger");

		this.filters = Arrays.asList(first, second);
	}

	/**
	 * @see #144
	 */
	@Test
	public void supportsIfAllFiltersMatch() {

		OrderLine orderLine = mock(OrderLine.class);
		doReturn("Trigger").when(orderLine).getProductName();

		assertThat(LineItemFilter.shouldBeHandled(orderLine, filters)).isTrue();
	}

	/**
	 * @see #144
	 */
	@Test
	public void doesNotSupportIfOneFilterDoesntMatch() {

		OrderLine orderLine = mock(OrderLine.class);
		doReturn("Some name").when(orderLine).getProductName();

		assertThat(LineItemFilter.shouldBeHandled(orderLine, filters)).isFalse();
	}
}
