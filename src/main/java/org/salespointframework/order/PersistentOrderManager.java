package org.salespointframework.order;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.salespointframework.core.Streamable;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.time.Interval;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * @author Thomas Dedek
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Service
@Transactional
@RequiredArgsConstructor
class PersistentOrderManager<T extends Order> implements OrderManager<T> {

	private final @NonNull BusinessTime businessTime;
	private final @NonNull OrderRepository<T> orderRepository;
	private final @NonNull ApplicationEventPublisher publisher;

	/* 
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManager#save(org.salespointframework.order.Order)
	 */
	@Override
	public T save(T order) {

		Assert.notNull(order, "Order must be not null");

		if (order.getDateCreated() == null) {
			order.setDateCreated(businessTime.getTime());
		}

		return orderRepository.save(order);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManager#get(org.salespointframework.order.OrderIdentifier)
	 */
	@Override
	public Optional<T> get(OrderIdentifier orderIdentifier) {

		Assert.notNull(orderIdentifier, "orderIdentifier must not be null");
		return orderRepository.findOne(orderIdentifier);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManager#contains(org.salespointframework.order.OrderIdentifier)
	 */
	@Override
	public boolean contains(OrderIdentifier orderIdentifier) {

		Assert.notNull(orderIdentifier, "OrderIdentifier must not be null");
		return orderRepository.exists(orderIdentifier);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManager#findBy(org.salespointframework.time.Interval)
	 */
	@Override
	public Streamable<T> findBy(Interval interval) {
		return Streamable.of(orderRepository.findByDateCreatedBetween(interval.getStart(), interval.getEnd()));
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManager#findBy(org.salespointframework.order.OrderStatus)
	 */
	@Override
	public Streamable<T> findBy(OrderStatus orderStatus) {

		Assert.notNull(orderStatus, "OrderStatus must not be null");
		return Streamable.of(orderRepository.findByOrderStatus(orderStatus));
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManager#findBy(org.salespointframework.useraccount.UserAccount)
	 */
	@Override
	public Streamable<T> findBy(UserAccount userAccount) {

		Assert.notNull(userAccount, "UserAccount must not be null");
		return Streamable.of(orderRepository.findByUserAccount(userAccount));
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManager#findBy(org.salespointframework.useraccount.UserAccount, org.salespointframework.time.Interval)
	 */
	@Override
	public Streamable<T> findBy(UserAccount userAccount, Interval interval) {

		Assert.notNull(userAccount, "UserAccount must not be null");
		Assert.notNull(interval, "Interval must not be null!");

		return Streamable.of(
				orderRepository.findByUserAccountAndDateCreatedBetween(userAccount, interval.getStart(), interval.getEnd()));
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManager#completeOrder(org.salespointframework.order.Order)
	 */
	@Override
	public void completeOrder(final T order) {

		Assert.notNull(order, "Order must not be null!");

		if (!order.isPaid()) {
			throw new OrderCompletionFailure(order, "Order is not paid yet!");
		}

		publisher.publishEvent(order.complete());

		save(order);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManager#pay(org.salespointframework.order.Order)
	 */
	@Override
	public boolean payOrder(T order) {

		Assert.notNull(order, "order must not be null");

		if (!order.isPaymentExpected()) {
			return false;
		}

		publisher.publishEvent(order.markPaid());

		save(order);

		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManager#cancelOrder(org.salespointframework.order.Order)
	 */
	@Override
	public boolean cancelOrder(T order) {

		Assert.notNull(order, "order must not be null");

		if (order.isOpen()) {
			order.cancel();
			return true;
		} else {
			return false;
		}
	}
}
