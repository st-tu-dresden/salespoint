/*
 * Copyright 2017-2018 the original author or authors.
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

	@Test // #144
	public void supportsIfAllFiltersMatch() {

		OrderLine orderLine = mock(OrderLine.class);
		doReturn("Trigger").when(orderLine).getProductName();

		assertThat(LineItemFilter.shouldBeHandled(orderLine, filters)).isTrue();
	}

	@Test // #144
	public void doesNotSupportIfOneFilterDoesntMatch() {

		OrderLine orderLine = mock(OrderLine.class);
		doReturn("Some name").when(orderLine).getProductName();

		assertThat(LineItemFilter.shouldBeHandled(orderLine, filters)).isFalse();
	}
}
