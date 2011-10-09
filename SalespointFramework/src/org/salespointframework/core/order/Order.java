package org.salespointframework.core.order;

import org.joda.time.DateTime;
import org.salespointframework.core.accountancy.Accountancy;
import org.salespointframework.core.accountancy.ProductPaymentEntry;
import org.salespointframework.core.money.Money;
import org.salespointframework.util.ArgumentNullException;

//TODO if you have really time, check if generics are really needed.
//OrderLine is an embeddable and has an elementcollection, so that may be an issue. 
//^
// yeah that is an issue, FUUUUUUUUU, possible solution save orderline-elementcollection as blob?

/**
 * Order interface
 * 
 * @author Thomas Dedek
 * @author Paul Henke
 *
 * @param <O>
 */
public interface Order<O extends OrderLine>
{
	/**
	 * Adds a new {@link OrderLine} to this order.
	 * An OrderLine can only be added if the {@link OrderStatus} is OPEN 
	 * @param orderLine the {@link OrderLine} to be added
	 * @return true if this Order did not already contain this {@link OrderLine}, otherwise false
	 * @throws ArgumentNullException if orderLine is null
	 */
	boolean addOrderLine(O orderLine);
	
	/**
	 * Removes an {@link OrderLine} from this order
	 * An OrderLine can only be removed if the {@link OrderStatus}s is OPEN
	 * @param orderLineIdentifier the identifier of the {@link OrderLine} to be removed
	 * @return true if this order contained the {@link OrderLine}, otherwise false
	 * @throws ArgumentNullException if orderLineIdentifier is null
	 */
	boolean removeOrderLine(OrderLineIdentifier orderLineIdentifier);
	
	/**
	 * 
	 * @return an Iterable of all {@link OrderLine}s from this order
	 */
	Iterable<O> getOrderLines();

	/**
	 * 
	 * @return the unique {@link OrderIdentifier} of this order
	 */
	
	OrderIdentifier getIdentifier();
	
	/**
	 * Adds a new {@link ChargeLine} to the order
	 * A ChargeLine can only be added if the {@link OrderStatus} is OPEN
	 * @param chargeLine the {@link ChargeLine} to be added
	 * @return true if this Order did not already contain this {@link ChargeLine}, otherwise false
	 * @throws ArgumentNullException if chargeLine is null
	 */
	boolean addChargeLine(ChargeLine chargeLine);
	
	/**
	 * Removes a {@link ChargeLine} from this order
	 * A ChargeLine can only be removed if the {@link OrderStatus} is OPEN
	 * @param chargeLineIdentifier the identifier of the {@link ChargeLine} to be removed
	 * @return true if this order contained the {@link ChargeLine}, otherwise false
	 * @throws ArgumentNullException if chargeLineIdentifier is null
	 */
	boolean removeChargeLine(ChargeLineIdentifier chargeLineIdentifier);
	
	/**
	 * 
	 * @return an Iterable of all {@link ChargeLine}s from this order
	 */
	Iterable<ChargeLine> getChargeLines();

	/**
	 * Returns the status [OPEN|CANCELLED|PAYED|COMPLETED] of this order
	 * @return The {@link OrderStatus} of the order
	 */
	OrderStatus getOrderStatus();
	
	/**
	 * Tries to complete this order, the {@link OrderStatus} has to be PAYED 
	 * @return an {@link OrderCompletionResult}
	 */
	OrderCompletionResult completeOrder();
	
	/**
	 * Cancels this order
	 * @return true if successful, otherwise false
	 */
	boolean cancelOrder();
	
	
	/**
	 * Calculates the total price of this Order. The number of ordered objects ({@link OrderLine}s) and {@link ChargeLine}s are included in the calculation.
	 * 
	 * @return the total price of this Order
	 */
	Money getTotalPrice();
	
	/**
	 * Calculates the price of all ordered objects ({@link OrderLine}s) from this Order without {@link ChargeLine}s.
	 * 
	 * @return the cumulative price of ordered objects from this Order
	 */
	Money getOrderedLinesPrice();
	
	/**
	 * Calculates the price of all {@link ChargeLine}s from this Order.
	 * 
	 * @return the charged price of this Order
	 */
	Money getChargeLinesPrice();
	
	/**
	 * Pays this order, an order needs to be payed before it can be completed.
	 * An {@link ProductPaymentEntry} is created for the {@link Accountancy}
	 * @return true if successful, otherwise false
	 */
	boolean payOrder();
	
	/**
	 * 
	 * @return a {@link DateTime} the order was created
	 */
	DateTime getDateCreated();
}
