package org.salespointframework.core.order;

import org.joda.time.DateTime;
import org.salespointframework.core.users.UserIdentifier;

/**
 * 
 * @author Thomas Dedek
 * @author Paul Henke
 * 
 */
public interface OrderManager<O extends Order<OL, CL>, OL extends OrderLine, CL extends ChargeLine>
{
	void add(O order);
	void remove(OrderIdentifier orderIdentifier);
	O get(OrderIdentifier orderIdentifier);
	boolean contains(OrderIdentifier orderIdentifier);
	// TODO mehr wie z.B. find(DateTime from, DateTime to) ???
	Iterable<O> find(OrderStatus orderStatus);
	Iterable<O> find(DateTime from, DateTime to);
	Iterable<O> find(UserIdentifier userIdentifier);
	Iterable<O> find(UserIdentifier userIdentifier, DateTime from, DateTime to);
}
