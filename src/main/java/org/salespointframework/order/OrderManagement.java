/*
 * Copyright 2017-2020 the original author or authors.
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

import java.util.Optional;

import org.salespointframework.order.Order.OrderCanceled;
import org.salespointframework.order.Order.OrderCompleted;
import org.salespointframework.order.Order.OrderPaid;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.time.Interval;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

/**
 * A service to manage {@link Order}s.
 *
 * @author Thomas Dedek
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Service
public interface OrderManagement<T extends Order> {

	/**
	 * Saves the given {@link Order} or persists changes to it.
	 *
	 * @param order the order to be saved, must not be {@literal null}.
	 * @return
	 */
	T save(T order);

	/**
	 * Returns the order identified by an {@link OrderIdentifier}
	 *
	 * @param orderIdentifier identifier of the {@link Order} to be returned, must not be {@literal null}.
	 * @return the order if the orderIdentifier matches, otherwise {@link Optional#empty()}.
	 */
	Optional<T> get(OrderIdentifier orderIdentifier);

	/**
	 * Checks if this {@link OrderManagement} contains an order.
	 *
	 * @param orderIdentifier the {@link OrderIdentifier} of the {@link Order}, must not be {@literal null}.
	 * @return {@literal true} if the OrderManager contains the order, {@literal false} otherwise.
	 */
	boolean contains(OrderIdentifier orderIdentifier);

	/**
	 * Returns all {@link Order}s having the {@link OrderStatus} {@code status}. If no orders with the specified status
	 * exist, an empty Iterable is returned.
	 *
	 * @param orderStatus Denoting the {@link OrderStatus} on which the {@link Order}s will be requested.
	 * @return a {@link Streamable} containing all {@link Order}s with the specified {@link OrderStatus}
	 */
	Streamable<T> findBy(OrderStatus orderStatus);

	/**
	 * Returns all {@link Order}s in between the {@link Interval}. So every entry with an time stamp <= to and >= from is
	 * returned. If no {@link Order}s within the specified time span exist, an empty {@link Iterable} is returned.
	 *
	 * @param interval The time interval to find {@link Order}s in, must not be {@literal null}.
	 * @return a {@link Streamable} containing all {@link Order}s in the given {@link Interval}.
	 */
	Streamable<T> findBy(Interval interval);

	/**
	 * Returns all {@link Order}s of the given {@link UserAccount}. If this user has no orders, an empty {@link Iterable}
	 * is returned.
	 *
	 * @param userAccount Denoting the {@link UserAccount} on which the orders will be requested, must not be
	 *          {@literal null}.
	 * @return a {@link Streamable} containing all orders of the specified user.
	 */
	Streamable<T> findBy(UserAccount userAccount);

	/**
	 * Returns all {@link Order}s from the given {@link UserAccount} in between the dates {@code from} and {@code to},
	 * including from and to. So every entry with an time stamp <= to and >= from is returned. If this user has no
	 * {@link Order}s in this period, an empty {@link Iterable} is returned.
	 *
	 * @param userAccount The {@link UserAccount} whose {@link Order}s shall be returned, must not be {@literal null}.
	 * @param interval The time interval to find {@link Order}s in, must not be {@literal null}.
	 * @return a {@link Streamable} containing all orders from the specified user in the specified period.
	 */
	Streamable<T> findBy(UserAccount userAccount, Interval interval);

	/**
	 * Tries to complete this order, the {@link OrderStatus} has to be {@link OrderStatus#PAID}.
	 *
	 * @param order the order to complete, must not be {@literal null}.
	 * @throws OrderCompletionFailure in case the order can't be completed.
	 * @see OrderCompleted
	 */
	void completeOrder(T order) throws OrderCompletionFailure;

	/**
	 * Pays the {@link Order}, {@link OrderStatus} must be {@link OrderStatus#OPEN} and {@link PaymentMethod} must be set.
	 *
	 * @param order the order to be payed, must not be {@literal null}.
	 * @return true if the order could be paid
	 * @see OrderPaid
	 */
	boolean payOrder(T order);

	/**
	 * Cancels an {@link Order}, no matter what state it is in.
	 *
	 * @param order the order to be canceled, must not be {@literal null}.
	 * @param reason the reason the order was cancelled.
	 * @return true if the order could be canceled
	 * @see OrderCanceled
	 */
	boolean cancelOrder(T order, String reason);

	/**
	 * Deletes the given {@link Order}.
	 *
	 * @param order must not be {@literal null}.
	 * @return the deleted {@link Order}
	 * @since 7.1
	 */
	T delete(T order);

	/**
	 * Returns the {@link Page} of orders specified by the given {@link Pageable}.
	 *
	 * @return the {@link Page} of orders specified by the given {@link Pageable}.
	 * @since 7.1
	 */
	Page<T> findAll(Pageable pageable);
}
