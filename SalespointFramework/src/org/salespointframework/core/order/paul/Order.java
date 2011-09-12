package org.salespointframework.core.order.paul;

import org.joda.time.DateTime;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.order.OrderLineIdentifier;
import org.salespointframework.core.order.OrderLogEntry;
import org.salespointframework.core.order.OrderStatus;

/**
 * 
 * @author Thomas Dedek
 * @author Paul Henke
 *
 */
public interface Order<O extends OrderLine, C extends ChargeLine> {
	
	boolean addOrderLine(O orderLine);
	boolean removeOrderLine(OrderLineIdentifier orderLineIdentifier);
	boolean addChargeLine(C chargeLine);
	boolean removeChargeLine(ChargeLineIdentifier chargeLineIdentifier);
	DateTime getCreationDate();
	String getTermsAndConditions();
	Iterable<O> getOrderLines();
	Iterable<C> getChargeLines();
	OrderStatus getOrderStatus();
	OrderCompletionResult completeOrder();
	void cancelOrder();
	Iterable<OrderLogEntry> getLogEntries();
	Money getTotalPrice();
	Money getOrderedLinesPrice();
	Money getChargeLinesPrice();
	
	
}
