package org.salespointframework.core.order;

import org.joda.time.DateTime;

public interface OrderManager {

	/**
	 * Add an <code>OrderEntry</code> to this
	 * <code>OrderManager</code> and persists them to underlying database.
	 * 
	 * @param order The <code>OrderEntry</code> which shall be added.
	 */
	public abstract void addOrder(OrderEntry order);

	/**
	 * Returns the <code>OrderEntry</code>s whith the specified
	 * <code>orderIdentifier</code>.
	 * 
	 * @param orderIdentifier
	 *            Denoting the identifier of the requested OrderEntry.
	 * @return The requested OrderEntry
	 */
	public abstract OrderEntry findOrder(OrderIdentifier orderIdentifier);

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
	public abstract Iterable<OrderEntry> findOrders(DateTime from, DateTime to);

	/**
	 * Returns all <code>OrderEntry</code>s having the OrderEntryStatus
	 * <code>status</code>. If no entries
	 * with the specified status exist, an empty Iterable is returned.
	 * 
	 * @param status
	 *            Denoting the OrderEntryStatus on which the OrderEntrys will be requested.
	 * @return an unmodifiable Iterable containing all OrderEntries whith the specified OrderEntryStatus
	 */
	public abstract Iterable<OrderEntry> findOrders(OrderStatus status);

	/**
	 * Remove an <code>OrderEntry</code> from this
	 * <code>OrderManager</code> and the underlying database.
	 * 
	 * @param orderIdentifier The Identifier of the <code>OrderEntry</code> which shall be removed.
	 */
	public abstract void remove(OrderIdentifier orderIdentifier);

	/**
	 * Remove an <code>OrderEntry</code> from this
	 * <code>OrderManager</code> and the underlying database.
	 * 
	 * @param orderEntry The <code>OrderEntry</code> which shall be removed.
	 */
	public abstract void remove(OrderEntry orderEntry);

	/**
	 * Commits changes from the given <code>OrderEntry</code> to the database.
	 * 
	 * @param orderEntry The <code>OrderEntry</code> which shall be updated.
	 */
	public abstract void update(OrderEntry orderEntry);

}