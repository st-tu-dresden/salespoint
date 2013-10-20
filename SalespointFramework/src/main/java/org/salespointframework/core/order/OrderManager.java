package org.salespointframework.core.order;

import org.joda.time.DateTime;
import org.salespointframework.core.useraccount.UserAccountIdentifier;

/**
 * OrderManager interface
 * 
 * @author Thomas Dedek
 * @author Paul Henke
 * 
 */
public interface OrderManager
{
	/**
	 * Adds a new {@link Order} to the OrderManager
	 * @param order the {@link Order} to be added
	 * @throws NullPointerException if order is null
	 */
	void add(Order order);
	
	/**
	 * Returns the order identified by an {@link OrderIdentifier}
	 * @param orderIdentifier identifier of the {@link Order} to be returned
	 * @return the order if the orderIdentifier matches, otherwise null
	 * @throws NullPointerException if orderIdentifier is null
	 */
	<E extends Order> E get(Class<E> clazz, OrderIdentifier orderIdentifier);
	
	/**
	 * Checks if this OrderManager contains an order
	 * @param orderIdentifier the {@link OrderIdentifier} of the {@link Order}
	 * @return true if the OrderManager contains the order, false otherwise
	 * @throws NullPointerException if orderIdentifier is null
	 */
	boolean contains(OrderIdentifier orderIdentifier);

	/**
	 * Returns all {@link Order}s having the {@link OrderStatus}
	 * <code>status</code>. If no orders
	 * with the specified status exist, an empty Iterable is returned.
	 * @param orderStatus
	 *            Denoting the {@link OrderStatus} on which the {@link Order}s will be requested.
	 * @return an Iterable containing all {@link Order}s with the specified {@link OrderStatus}
	 * @throws NullPointerException if orderStatus is null
	 */
	<E extends Order> Iterable<E> find(Class<E> clazz, OrderStatus orderStatus);
	
	/**
	 * Returns all {@link Order}s in between the dates
	 * <code>from</code> and <code>to</code>, including from and to. So every
	 * entry with an time stamp <= to and >= from is returned. If no orders
	 * within the specified time span exist, an empty Iterable is returned.
	 * 
	 * @param from
	 *            time stamp denoting the start of the requested time period
	 * @param to
	 *            time stamp denoting the end of the requested time period
	 * @return an Iterable containing all {@link Order}s between from and
	 *         to
	 * @throws NullPointerException if from or to are null         
	 */
	<E extends Order> Iterable<E> find(Class<E> clazz, DateTime from, DateTime to);
	
	/**
	 * Returns all {@link Order}s from the given {@link UserAccountIdentifier}. If this user
	 * has no orders, an empty Iterable is returned.
	 * 
	 * @param userIdentifier
	 *            Denoting the {@link UserAccountIdentifier} on which the orders will be
	 *            requested.
	 * @return an Iterable containing all orders from the
	 *         specified user.
	 * @throws NullPointerException if userIdentifier is null
	 */
	<E extends Order> Iterable<E> find(Class<E> clazz, UserAccountIdentifier userIdentifier);
	
	/**
	 * Returns all {@link Order}s from the given {@link UserAccountIdentifier} in between the
	 * dates <code>from</code> and <code>to</code>, including from and to. If
	 * this user has no orders in this period, an empty Iterable is
	 * returned.
	 * 
	 * @param userIdentifier
	 *            Denoting the {@link UserAccountIdentifier} on which the orders will be
	 *            requested.
	 * @param from
	 *            time stamp denoting the start of the requested time period
	 * @param to
	 *            time stamp denoting the end of the requested time period
	 * @return an Iterable containing all orders from the
	 *         specified user in the specified period.
	 * @throws NullPointerException if any argument is null         
	 */
	<E extends Order> Iterable<E> find(Class<E> clazz, UserAccountIdentifier userIdentifier, DateTime from, DateTime to);
	
	
	/**
	 * Tries to complete this order, the {@link OrderStatus} has to be PAYED 
	 * @param order TODO
	 * @return an {@link OrderCompletionResult}
	 */
	OrderCompletionResult completeOrder(Order order);

	void update(Order order);
	
	boolean pay(Order order);
}
