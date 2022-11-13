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

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.salespointframework.order.Order.OrderIdentifier;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.time.Interval;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
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
class PersistentOrderManagement<T extends Order> implements OrderManagement<T> {

	private final @NonNull BusinessTime businessTime;
	private final @NonNull OrderRepository<T> orderRepository;

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManagement#save(org.salespointframework.order.Order)
	 */
	@Override
	public T save(T order) {

		Assert.notNull(order, "Order must be not null");

		if (order.isNew()) {
			order.setDateCreated(businessTime.getTime());
		}

		return orderRepository.save(order);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManagement#get(org.salespointframework.order.OrderIdentifier)
	 */
	@Override
	public Optional<T> get(OrderIdentifier orderIdentifier) {

		Assert.notNull(orderIdentifier, "orderIdentifier must not be null");

		return orderRepository.findById(orderIdentifier);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManagement#contains(org.salespointframework.order.OrderIdentifier)
	 */
	@Override
	public boolean contains(OrderIdentifier orderIdentifier) {

		Assert.notNull(orderIdentifier, "OrderIdentifier must not be null");

		return orderRepository.existsById(orderIdentifier);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManagement#findBy(org.salespointframework.time.Interval)
	 */
	@Override
	public Streamable<T> findBy(Interval interval) {
		return orderRepository.findByDateCreatedBetween(interval.getStart(), interval.getEnd());
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManagement#findBy(org.salespointframework.order.OrderStatus)
	 */
	@Override
	public Streamable<T> findBy(OrderStatus orderStatus) {

		Assert.notNull(orderStatus, "OrderStatus must not be null");

		return orderRepository.findByOrderStatus(orderStatus);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManagement#findBy(org.salespointframework.useraccount.UserAccount)
	 */
	@Override
	public Streamable<T> findBy(UserAccount userAccount) {

		Assert.notNull(userAccount, "UserAccount must not be null");

		return orderRepository.findByUserAccount(userAccount);
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManagement#findBy(org.salespointframework.useraccount.UserAccount, org.salespointframework.time.Interval)
	 */
	@Override
	public Streamable<T> findBy(UserAccount userAccount, Interval interval) {

		Assert.notNull(userAccount, "UserAccount must not be null");
		Assert.notNull(interval, "Interval must not be null!");

		return orderRepository.findByUserAccountAndDateCreatedBetween(userAccount, interval.getStart(), interval.getEnd());
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManagement#completeOrder(org.salespointframework.order.Order)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void completeOrder(final T order) {

		Assert.notNull(order, "Order must not be null!");

		if (!order.isPaid()) {
			throw new OrderCompletionFailure(order, "Order is not paid yet!");
		}

		try {

			save((T) order.complete());

		} catch (RuntimeException o_O) {

			order.uncomplete();

			throw o_O;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManagement#payOrder(org.salespointframework.order.Order)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean payOrder(T order) {

		Assert.notNull(order, "Order must not be null");

		if (!order.isPaymentExpected()) {
			return false;
		}

		save((T) order.markPaid());

		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManagement#cancelOrder(org.salespointframework.order.Order, java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean cancelOrder(T order, String reason) {

		Assert.notNull(order, "Order must not be null");

		if (!order.isCanceled()) {
			save((T) order.cancel(reason));
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManagement#delete(org.salespointframework.order.Order)
	 */
	public T delete(T order) {

		Assert.notNull(order, "Order must not be null!");

		orderRepository.delete(order);

		return order;
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.OrderManagement#findAll(org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<T> findAll(Pageable pageable) {
		return orderRepository.findAll(pageable);
	}
}
