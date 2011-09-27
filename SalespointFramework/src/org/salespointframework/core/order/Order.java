package org.salespointframework.core.order;

import org.joda.time.DateTime;
import org.salespointframework.core.money.Money;

/**
 * 
 * @author Thomas Dedek
 * @author Paul Henke
 * 
 */
//TODO if you have really time, check if generics are really needed.
//OrderLine is an embeddable and has an elementcollection, so that may be an issue.
public interface Order<O extends OrderLine, C extends ChargeLine>
{

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
