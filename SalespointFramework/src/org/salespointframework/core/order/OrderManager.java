package org.salespointframework.core.order;

import org.joda.time.DateTime;
import org.salespointframework.core.users.UserIdentifier;

public interface OrderManager {

	/**
	 * Add an <code>OrderEntry</code> to this
	 * <code>OrderManager</code> and persists them to underlying database.
	 * 
	 * @param order The <code>OrderEntry</code> which shall be added.
	 */
	void addOrder(OrderEntry order);

	/**
	 * Returns the <code>OrderEntry</code>s whith the specified
	 * <code>orderIdentifier</code>.
	 * 
	 * @param orderIdentifier
	 *            Denoting the identifier of the requested OrderEntry.
	 * @return The requested OrderEntry
	 */
	OrderEntry findOrder(OrderIdentifier orderIdentifier);

	/**
	 * Returns all <code>OrderEntry</code>s in between the dates
	 * <code>from</code> and <code>to</code>, including from and to. So every
	 * entry with an time stamp <= to and >= from is returned. If no entries
	 * within the specified time span exist, an empty Iterable is returned.
	 * 
	 * @param from
	 *            time stamp denoting the start of the requested time period
	 * @param to
	 *            time stamp denoting the end of the requested time period
	 * @return an unmodifiable Iterable containing all OrderEntries between from and
	 *         to
	 */
	Iterable<OrderEntry> findOrders(DateTime from, DateTime to);

	/**
	 * Returns all <code>OrderEntry</code>s having the OrderEntryStatus
	 * <code>status</code>. If no entries
	 * with the specified status exist, an empty Iterable is returned.
	 * 
	 * @param status
	 *            Denoting the OrderEntryStatus on which the OrderEntrys will be requested.
	 * @return an unmodifiable Iterable containing all OrderEntries whith the specified OrderEntryStatus
	 */
	Iterable<OrderEntry> findOrders(OrderStatus status);

	/**
	 * Remove an <code>OrderEntry</code> from this
	 * <code>OrderManager</code> and the underlying database.
	 * 
	 * @param orderIdentifier The Identifier of the <code>OrderEntry</code> which shall be removed.
	 */
	OrderEntry remove(OrderIdentifier orderIdentifier);

	// TODO nötig??
	/**
	 * Commits changes from the given <code>OrderEntry</code> to the database.
	 * 
	 * @param orderEntry The <code>OrderEntry</code> which shall be updated.
	 */
	 void update(OrderEntry orderEntry);
	 
	/**
	 * Checks whether the specified user has initialized, open or processing
	 * orders. For example this information have to be checked before removing
	 * an user from the system.
	 * 
	 * @param userIdentifier
	 *            Denoting the UserIdentifier on which shall be checked.
	 * @return True, if the user has initialized, open or processing orders.
	 */
	 boolean hasOpenOrders(UserIdentifier userIdentifier);
	 
	/**
	 * Returns all <code>OrderEntry</code>s from the given userID. If this user
	 * has no orderEntries, an empty Iterable is returned.
	 * 
	 * @param userIdentifier
	 *            Denoting the UserIdentifier on which the OrderEntrys will be
	 *            requested.
	 * @return an unmodifiable Iterable containing all OrderEntries from the
	 *         specified user.
	 */
	 Iterable<OrderEntry> getOrders(UserIdentifier userIdentifier);
	 
	/**
	 * Returns all <code>OrderEntry</code>s from the given userID in between the
	 * dates <code>from</code> and <code>to</code>, including from and to. If
	 * this user has no orderEntries in this period, an empty Iterable is
	 * returned.
	 * 
	 * @param userIdentifier
	 *            Denoting the UserIdentifier on which the OrderEntrys will be
	 *            requested.
	 * @param from
	 *            time stamp denoting the start of the requested time period
	 * @param to
	 *            time stamp denoting the end of the requested time period
	 * @return an unmodifiable Iterable containing all OrderEntries from the
	 *         specified user in the specified period.
	 */
	 Iterable<OrderEntry> getOrders(UserIdentifier userIdentifier, DateTime from, DateTime to);

}