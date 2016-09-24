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
