package org.salespointframework.core.order;

import org.joda.time.DateTime;
import org.salespointframework.core.money.Money;

/**
 * 
 * @author Thomas Dedek
 * @author Paul Henke
 *
 */
public interface Order<O extends OrderLine, C extends ChargeLine> {
	
	boolean addOrderLine(O orderLine);
	boolean removeOrderLine(OrderLineIdentifier orderLineIdentifier);
	Iterable<O> getOrderLines();
	
	boolean addChargeLine(C chargeLine);
	boolean removeChargeLine(ChargeLineIdentifier chargeLineIdentifier);
	Iterable<C> getChargeLines();
	
	DateTime getCreationDate();
	String getTermsAndConditions();
	OrderStatus getOrderStatus();
	OrderCompletionResult completeOrder();
	boolean cancelOrder();
	Iterable<OrderLogEntry> getLogEntries();
	Money getTotalPrice();
	Money getOrderedLinesPrice();
	Money getChargeLinesPrice();
	boolean payOrder(/* PaymentMethod */);
}
