package org.salespointframework.core.order.paul;

import org.joda.time.DateTime;
import org.salespointframework.core.order.OrderIdentifier;
import org.salespointframework.core.order.OrderStatus;
import org.salespointframework.core.users.UserIdentifier;

/**
 * 
 * @author Thomas Dedek
 * @author Paul Henke
 *
 */
public interface OrderManager<O extends Order<OL,CL>, OL extends OrderLine, CL extends ChargeLine> {

	void add(O order);
	void remove(OrderIdentifier orderIdentifier);
	O get(OrderIdentifier orderIdentifier);
	boolean contains(OrderIdentifier orderIdentifier);
	
	Iterable<O> findOrders(DateTime from, DateTime to);
	Iterable<O> findOrders(OrderStatus status);
	Iterable<O> findOrders(UserIdentifier userIdentifier);
	Iterable<O> findOrders(UserIdentifier userIdentifier, DateTime from, DateTime to);

}
