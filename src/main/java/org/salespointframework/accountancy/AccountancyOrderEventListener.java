package org.salespointframework.accountancy;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.salespointframework.order.Order;
import org.salespointframework.order.Order.OrderCompleted;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * An {@link ApplicationListener} for {@link OrderCompleted} events to create {@link ProductPaymentEntry} for the
 * {@link Order}.
 * 
 * @author Oliver Gierke
 */
@Component
@RequiredArgsConstructor
public class AccountancyOrderEventListener {

	private final @NonNull Accountancy accountancy;

	@EventListener
	public void on(OrderCompleted event) {

		Order order = event.getOrder();

		accountancy.add(ProductPaymentEntry.of(order, "Rechnung Nr. ".concat(order.getId().toString())));
	}
}
