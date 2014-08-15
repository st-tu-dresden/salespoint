package org.salespointframework.order;

import java.time.LocalDateTime;
import java.util.Optional;

import org.salespointframework.accountancy.payment.PaymentMethod;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountIdentifier;

/**
 * OrderManager interface
 * 
 * @author Thomas Dedek
 * @author Paul Henke
 * @author Oliver Gierke
 */
public interface OrderManager<T extends Order>
{
	/**
	 * Adds a new {@link Order} to the OrderManager
	 * @param order the {@link Order} to be added
	 * @throws NullPointerException if order is null
	 */
	T add(T order);
	
	/**
	 * Returns the order identified by an {@link OrderIdentifier}
	 * @param orderIdentifier identifier of the {@link Order} to be returned
	 * @return the order if the orderIdentifier matches, otherwise null
	 * @throws NullPointerException if orderIdentifier is null
	 */
	Optional<T> get(OrderIdentifier orderIdentifier);
	
	/**
	 * Checks if this OrderManager contains an order
	 * @param orderIdentifier the {@link OrderIdentifier} of the {@link Order}
	 * @return true if the OrderManager contains the order, false otherwise
	 * @throws NullPointerException if orderIdentifier is null
	 */
	boolean contains(OrderIdentifier orderIdentifier);

	/**
	 * Returns all {@link Order}s having the {@link OrderStatus}
	 * {@code status}. If no orders
	 * with the specified status exist, an empty Iterable is returned.
	 * @param orderStatus
	 *            Denoting the {@link OrderStatus} on which the {@link Order}s will be requested.
	 * @return an Iterable containing all {@link Order}s with the specified {@link OrderStatus}
	 * @throws NullPointerException if orderStatus is null
	 */
	Iterable<T> find(OrderStatus orderStatus);
	
	/**
	 * Returns all {@link Order}s in between the dates
	 * {@code from} and {@code to}, including from and to. So every
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
	Iterable<T> find(LocalDateTime from, LocalDateTime to);
	
	/**
	 * Returns all {@link Order}s from the given {@link UserAccountIdentifier}. If this user
	 * has no orders, an empty Iterable is returned.
	 * 
	 * @param userAccount
	 *            Denoting the {@link UserAccount} on which the orders will be
	 *            requested.
	 * @return an Iterable containing all orders from the
	 *         specified user.
	 * @throws NullPointerException if clazz oder userAccount are null
	 */
	 Iterable<T> find(UserAccount userAccount);
	
	/**
	 * Returns all {@link Order}s from the given {@link UserAccountIdentifier} in between the
	 * dates {@code from} and {@code to}, including from and to. If
	 * this user has no orders in this period, an empty Iterable is
	 * returned.
	 * 
	 * @param userAccount
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
	Iterable<T> find(UserAccount userAccount, LocalDateTime from, LocalDateTime to);
	
	/**
	 * Tries to complete this order, the {@link OrderStatus} has to be PAYED 
	 * @param order the order to complete
	 * @return an {@link OrderCompletionResult}
	 */
	OrderCompletionResult completeOrder(T order);

	/**
	 * Updates and persists an existing {@link Order} to the PersistentOrderManager and the Database
	 * @param order the {@link Order} to be updated
	 * @throws NullPointerException if order is null
	 */
	void update(T order);
	
	/**
	 * Pays the {@link Order}, {@link OrderStatus} must be OPEN and {@link PaymentMethod} must be set
	 * @param order the order to be payed
	 * @return true if the order could be payed
	 * @throws NullPointerException if order is null
	 */
	boolean payOrder(T order);
	
	/**
	 * Cancels an {@link Order}, it can only be cancelled is {@link OrderStatus} is OPEN
	 * @param order the order to be canceled
	 * @return true if the order could be canceled
	 * @throws NullPointerException if order is null
	 */
	boolean cancelOrder(T order);
}
