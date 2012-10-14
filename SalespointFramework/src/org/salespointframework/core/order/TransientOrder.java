package org.salespointframework.core.order;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.joda.time.DateTime;
import org.salespointframework.core.accountancy.Accountancy;
import org.salespointframework.core.accountancy.TransientAccountancy;
import org.salespointframework.core.accountancy.TransientProductPaymentEntry;
import org.salespointframework.core.accountancy.payment.PaymentMethod;
import org.salespointframework.core.inventory.Inventory;
import org.salespointframework.core.inventory.TransientInventory;
import org.salespointframework.core.inventory.TransientInventoryItem;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.order.OrderCompletionResult.OrderCompletionStatus;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.quantity.Quantity;
import org.salespointframework.core.shop.Shop;
import org.salespointframework.core.user.User;
import org.salespointframework.core.user.UserIdentifier;
import org.salespointframework.util.Iterables;

public class TransientOrder implements Order<TransientOrderLine>, Comparable<TransientOrder> {

	
	private final OrderIdentifier orderIdentifier = new OrderIdentifier();

	private PaymentMethod paymentMethod;

	private final UserIdentifier userIdentifier;

	private final DateTime dateCreated = Shop.INSTANCE.getTime().getDateTime();

	private OrderStatus orderStatus = OrderStatus.OPEN;

	private final Set<TransientOrderLine> orderLines = new HashSet<>();

	private final Set<ChargeLine> chargeLines = new HashSet<>();
	
	public TransientOrder(UserIdentifier userIdentifier, PaymentMethod paymentMethod) {
		this.userIdentifier = Objects.requireNonNull(userIdentifier, "userIdentifier must not be null");
		this.paymentMethod = Objects.requireNonNull(paymentMethod, "paymentMethod must not be null");
	}
	
	/**
	 * Creates a new TransientOrder
	 * 
	 * @param userIdentifier
	 *            The {@link UserIdentifier}/{@link User} connected to this
	 *            order
	 * @throws NullPointerException if userIdentifier is null
	 */
	public TransientOrder(UserIdentifier userIdentifier) {
		this.userIdentifier = Objects.requireNonNull(userIdentifier, "userIdentifier must not be null");
	}
	
	@Override
	public boolean addOrderLine(TransientOrderLine orderLine) {
		Objects.requireNonNull(orderLine, "orderLine must not be null");
		if (orderStatus != OrderStatus.OPEN) {
			return false;
		}
		return orderLines.add(orderLine);
	}

	@Override
	public boolean removeOrderLine(OrderLineIdentifier orderLineIdentifier) {
		Objects.requireNonNull(orderLineIdentifier, "orderLineIdentifier must not be null");
		if (orderStatus != OrderStatus.OPEN) {
			return false;
		}
		TransientOrderLine temp = null;
		for (TransientOrderLine pol : orderLines) {
			if (pol.getIdentifier().equals(orderLineIdentifier)) {
				temp = pol;
				break;
			}
		}
		return orderLines.remove(temp);
	}

	@Override
	public boolean addChargeLine(ChargeLine chargeLine) {
		Objects.requireNonNull(chargeLine, "chargeLine must not be null");
		if (orderStatus != OrderStatus.OPEN) {
			return false;
		}
		return chargeLines.add(chargeLine);
	}

	@Override
	public boolean removeChargeLine(ChargeLineIdentifier chargeLineIdentifier) {
		Objects.requireNonNull(chargeLineIdentifier, "chargeLineIdentifier must not be null");
		
		if (orderStatus != OrderStatus.OPEN) {
			return false;
		}
		ChargeLine temp = null;
		for (ChargeLine pcl : chargeLines) {
			if (pcl.getIdentifier().equals(chargeLineIdentifier)) {
				temp = pcl;
				break;
			}
		}
		return chargeLines.remove(temp);
	}

	@Override
	public Iterable<TransientOrderLine> getOrderLines() {
		return Iterables.of(orderLines);
	}

	@Override
	public Iterable<ChargeLine> getChargeLines() {
		return Iterables.of(chargeLines);
	}

	@Override
	public final OrderStatus getOrderStatus() {
		return orderStatus;
	}
	
	@Override
	public final UserIdentifier getUserIdentifier() {
		return userIdentifier;
	}

	@Override
	public boolean cancelOrder() {
		if (orderStatus == OrderStatus.OPEN) {
			orderStatus = OrderStatus.CANCELLED;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Money getTotalPrice() {
		return this.getOrderedLinesPrice().add(this.getChargeLinesPrice());
	}

	@Override
	public Money getOrderedLinesPrice() {
		Money price = Money.ZERO;
		for (OrderLine orderLine : orderLines) {
			price = price.add(orderLine.getPrice());
		}
		return price;
	}

	@Override
	public Money getChargeLinesPrice() {
		Money price = Money.ZERO;
		for(ChargeLine chargeLine : chargeLines) {
			price = price.add(chargeLine.getPrice());
		}
		return price;
	}

	@Override
	public final boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (other == this) {
			return true;
		}
		if (other instanceof TransientOrder) {
			return this.orderIdentifier.equals(((TransientOrder)other).orderIdentifier);
		}
		return false;
	}

	@Override
	public final int hashCode() {
		return this.orderIdentifier.hashCode();
	}

	@Override
	public String toString() {
		return "User: " + userIdentifier.toString() + " | Order" + orderIdentifier.toString();
	}

	@Override
	public final DateTime getDateCreated() {
		return dateCreated;
	}

	@Override
	public OrderCompletionResult completeOrder() {
		
		Inventory<?> temp = Shop.INSTANCE.getInventory();
		
		if(temp == null) 
		{
			throw new NullPointerException("Shop.INSTANCE.getInventory() returned null");
		}
		
		if (!(temp instanceof TransientInventory)) 
		{
			throw new RuntimeException("Sorry, TransientOrder works only with TransientInventory :(");
		}
		
		TransientInventory inventory = (TransientInventory) temp;
		
		Map<TransientInventoryItem, Quantity> goodItems = new HashMap<>();
		Map<TransientInventoryItem, Quantity> badItems = new HashMap<>();
		
		for(TransientOrderLine orderline : orderLines) {
			ProductIdentifier productIdentifier =  orderline.getProductIdentifier();
			TransientInventoryItem inventoryItem = inventory.getByProductIdentifier(TransientInventoryItem.class, productIdentifier); 
			
			// TODO was machen wenn nicht im Inventar
			if(inventoryItem == null) { 
				System.out.println("item null");
				break;
			}
			if(!inventoryItem.getQuantity().subtract(orderline.getQuantity()).isNegative()) {
				goodItems.put(inventoryItem, orderline.getQuantity());
			} else {
				badItems.put(inventoryItem, orderline.getQuantity());
			}
		}
		// TODO mehr ACID oder egal!?
		if(goodItems.size() == orderLines.size()) {
			System.out.println("size == ");
			for(TransientInventoryItem inventoryItem : goodItems.keySet()) {
				Quantity quantity = goodItems.get(inventoryItem);
				inventoryItem.decreaseQuantity(quantity);
			}
			this.orderStatus = OrderStatus.COMPLETED;
			return new InternalOrderCompletionResult(OrderCompletionStatus.SUCCESSFUL);
		} else {
			System.out.println("size != ");
			return new InternalOrderCompletionResult(OrderCompletionStatus.FAILED);
		}
	}
	
	private final class InternalOrderCompletionResult implements OrderCompletionResult 
	{
		private final OrderCompletionStatus status;
		
		public InternalOrderCompletionResult(OrderCompletionStatus status) {
			this.status = status;
		}
		
		@Override
		public OrderCompletionStatus getStatus()
		{
			return status;
		}
	}

		
		
	public boolean isPayed() {
		return orderStatus == OrderStatus.PAYED;
	}

	/**
	 * Convenience method for checking if an order has the status CANCELLED
	 * 
	 * @return true if OrderStatus is CANCELLED, otherwise false
	 */
	public boolean isCanceled() {
		return orderStatus == OrderStatus.CANCELLED;
	}

	/**
	 * Convenience method for checking if an order has the status COMPLETED
	 * 
	 * @return true if OrderStatus is COMPLETED, otherwise false
	 */
	public boolean isCompleted() {
		return orderStatus == OrderStatus.COMPLETED;
	}

	/**
	 * Convenience method for checking if an order has the status OPEN
	 * 
	 * @return true if OrderStatus is OPEN, otherwise false
	 */
	public boolean isOpen() {
		return orderStatus == OrderStatus.OPEN;
	}

	@Override
	public boolean payOrder() {
		if (orderStatus != OrderStatus.OPEN || paymentMethod == null) 
		{
			return false;
		}
		
		Accountancy<?> temp = Shop.INSTANCE.getAccountancy();

		if(temp == null) 
		{
			throw new NullPointerException("Shop.INSTANCE.getAccountancy() returned null");
		}
		
		if(!(temp instanceof TransientAccountancy)) 
		{
			throw new RuntimeException("Sorry, TransientOrder works only with TransientAccountancy :(");
		}
		
		TransientAccountancy accountancy = (TransientAccountancy) temp;

		// TODO "Rechnung Nr " deutsch?
		TransientProductPaymentEntry ppe = new TransientProductPaymentEntry(this.orderIdentifier, this.userIdentifier, this.getTotalPrice(), "Rechnung Nr. " + this.orderIdentifier, this.paymentMethod);
		accountancy.add(ppe);
		orderStatus = OrderStatus.PAYED;
		return true;
	}

	@Override
	public int compareTo(TransientOrder other) {
		return this.orderIdentifier.compareTo(other.orderIdentifier);
	}

	@Override
	public final OrderIdentifier getIdentifier() {
		return orderIdentifier;
	}

	@Override
	public final PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}
	
	public void setPaymentMethod(PaymentMethod paymentMethod) {
		if(orderStatus != OrderStatus.OPEN) return;
		this.paymentMethod = Objects.requireNonNull(paymentMethod,"paymentMethod must not be null");
	}
}