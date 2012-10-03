package org.salespointframework.core.order;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.RollbackException;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.DateTime;
import org.salespointframework.core.accountancy.PersistentAccountancy;
import org.salespointframework.core.accountancy.PersistentProductPaymentEntry;
import org.salespointframework.core.accountancy.TransientAccountancy;
import org.salespointframework.core.accountancy.TransientProductPaymentEntry;
import org.salespointframework.core.accountancy.payment.PaymentMethod;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.inventory.Inventory;
import org.salespointframework.core.inventory.PersistentInventory;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.order.OrderCompletionResult.OrderCompletionStatus;
import org.salespointframework.core.product.PersistentProductInstance;
import org.salespointframework.core.product.ProductInstance;
import org.salespointframework.core.shop.Shop;
import org.salespointframework.core.user.User;
import org.salespointframework.core.user.UserIdentifier;
import org.salespointframework.util.Iterables;

public class TransientOrder implements Order<TransientOrderLine>, Comparable<TransientOrder> {

	
	private OrderIdentifier orderIdentifier = new OrderIdentifier();

	private PaymentMethod paymentMethod;

	private UserIdentifier userIdentifier;

	private Date dateCreated = Shop.INSTANCE.getTime().getDateTime().toDate();

	private OrderStatus orderStatus = OrderStatus.OPEN;

	private Set<TransientOrderLine> orderLines = new HashSet<>();

	private Set<ChargeLine> chargeLines = new HashSet<>();
	
	public TransientOrder(UserIdentifier userIdentifier, PaymentMethod paymentMethod) {
		this.userIdentifier = Objects.requireNonNull(userIdentifier, "userIdentifier must not be null");
		this.paymentMethod = Objects.requireNonNull(paymentMethod, "paymentMethod must not be null");
	}
	
	/**
	 * Creates a new PersistentOrder
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
		return new DateTime(dateCreated);
	}

	
	// TODO
	@Override
	public OrderCompletionResult completeOrder() {
		return null;
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
		if (orderStatus != OrderStatus.OPEN || paymentMethod == null) {
			return false;
		}

		// TODO "Rechnung Nr " deutsch?
		// TODO TransientAcc
		TransientProductPaymentEntry ppe = new TransientProductPaymentEntry(this.orderIdentifier, this.userIdentifier, this.getTotalPrice(), "Rechnung Nr. " + this.orderIdentifier, this.paymentMethod);
		TransientAccountancy accountancy = (TransientAccountancy) Shop.INSTANCE.getAccountancy();
		if(accountancy == null) {
			throw new NullPointerException("Shop.INSTANCE.getAccountancy() returned null");
		}
		accountancy.add(ppe);
		orderStatus = OrderStatus.PAYED;
		return true;
	}

	// TODO
	private final class InternalOrderCompletionResult implements OrderCompletionResult {

		@Override
		public OrderCompletionStatus getStatus()
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean rollBack()
		{
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Order<OrderLine> splitOrder()
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Iterable<ProductInstance> getRemovedInstances()
		{
			// TODO Auto-generated method stub
			return null;
		}

	
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
		this.paymentMethod = Objects.requireNonNull(paymentMethod, "paymentMethod");
	}
}