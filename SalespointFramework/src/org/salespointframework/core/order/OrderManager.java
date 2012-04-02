package org.salespointframework.core.order;

import org.joda.time.DateTime;
import org.salespointframework.core.user.UserIdentifier;
import org.salespointframework.util.ArgumentNullException;

// TODO comment new addition Class<T>
/**
 * OrderManager interface
 * 
 * @author Thomas Dedek
 * @author Paul Henke
 * 
 *
 * @param <O> generic order paramter 
 * @param <OL> generic orderline parameter
 */
public interface OrderManager<O extends Order<OL>, OL extends OrderLine>
{
	/**
	 * Adds a new {@link Order} to the OrderManager
	 * @param order the {@link Order} to be added
	 * @throws ArgumentNullException if order is null
	 */
	void add(O order);
	
	/**
	 * Returns the order identified by an {@link OrderIdentifier}
	 * @param orderIdentifier identifier of the {@link Order} to be returned
	 * @return the order if the orderIdentifier matches, otherwise null
	 * @throws ArgumentNullException if orderIdentifier is null
	 */
	<E extends O> E get(Class<E> clazz, OrderIdentifier orderIdentifier);
	
	/**
	 * Checks if this OrderManager contains an order
	 * @param orderIdentifier the {@link OrderIdentifier} of the {@link Order}
	 * @return true if the OrderManager contains the order, false otherwise
	 * @throws ArgumentNullException if orderIdentifier is null
	 */
	boolean contains(OrderIdentifier orderIdentifier);

	/**
	 * Returns all {@link Order}s having the {@link OrderStatus}
	 * <code>status</code>. If no orders
	 * with the specified status exist, an empty Iterable is returned.
	 * @param orderStatus
	 *            Denoting the {@link OrderStatus} on which the {@link Order}s will be requested.
	 * @return an Iterable containing all {@link Order}s with the specified {@link OrderStatus}
	 * @throws ArgumentNullException if orderStatus is null
	 */
	<E extends O> Iterable<E> find(Class<E> clazz, OrderStatus orderStatus);
	
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
	 * @throws ArgumentNullException if from or to are null         
	 */
	<E extends O> Iterable<E> find(Class<E> clazz, DateTime from, DateTime to);
	
	/**
	 * Returns all {@link Order}s from the given {@link UserIdentifier}. If this user
	 * has no orders, an empty Iterable is returned.
	 * 
	 * @param userIdentifier
	 *            Denoting the {@link UserIdentifier} on which the orders will be
	 *            requested.
	 * @return an Iterable containing all orders from the
	 *         specified user.
	 * @throws ArgumentNullException if userIdentifier is null
	 */
	<E extends O> Iterable<E> find(Class<E> clazz, UserIdentifier userIdentifier);
	
	/**
	 * Returns all {@link Order}s from the given {@link UserIdentifier} in between the
	 * dates <code>from</code> and <code>to</code>, including from and to. If
	 * this user has no orders in this period, an empty Iterable is
	 * returned.
	 * 
	 * @param userIdentifier
	 *            Denoting the {@link UserIdentifier} on which the orders will be
	 *            requested.
	 * @param from
	 *            time stamp denoting the start of the requested time period
	 * @param to
	 *            time stamp denoting the end of the requested time period
	 * @return an Iterable containing all orders from the
	 *         specified user in the specified period.
	 * @throws ArgumentNullException if any argument is null         
	 */
	<E extends O> Iterable<E> find(Class<E> clazz, UserIdentifier userIdentifier, DateTime from, DateTime to);
}
