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

import java.util.Collection;
import java.util.function.Predicate;

import org.salespointframework.order.OrderLine;
import org.springframework.util.Assert;

/**
 * A {@link Predicate} to allow defining whether the {@link OrderLine}s should be post processed by the
 * {@link InventoryOrderEventListener}. The filters are affirmative so evaluating the {@link Predicate} to
 * {@literal false} will let the given {@link OrderLine} not be post processed by the listener.
 * 
 * @author Oliver Gierke
 * @since 6.3
 */
public interface LineItemFilter extends Predicate<OrderLine> {

	/**
	 * Returns whether the given {@link OrderLine} should be handled considering the given {@link LineItemFilter}s. This
	 * means as soon as one of the filters returns {@literal false}, it's not handled by the listener anymore and
	 * considered valid.
	 * 
	 * @param orderLine must not be {@literal null}.
	 * @param filters must not be {@literal null}.
	 * @return
	 */
	static boolean shouldBeHandled(OrderLine orderLine, Collection<LineItemFilter> filters) {

		Assert.notNull(orderLine, "Order line must not be null!");
		Assert.notNull(filters, " must not be null!");

		return filters.stream().allMatch(predicate -> predicate.test(orderLine));
	}

	/**
	 * Returns a {@link LineItemFilter} that accepts all {@link OrderLine}s.
	 * 
	 * @return will never be {@literal null}.
	 */
	static LineItemFilter handleAll() {
		return item -> true;
	}

	/**
	 * Returns a {@link LineItemFilter} that does not consider any {@link OrderLine}s.
	 * 
	 * @return will never be {@literal null}.
	 */
	static LineItemFilter handleNone() {
		return item -> false;
	}
}
