package org.salespointframework.order;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.salespointframework.accountancy.ProductPaymentEntry;
import org.salespointframework.accountancy.payment.PaymentMethod;
import org.salespointframework.core.AbstractEntity;
import org.salespointframework.useraccount.UserAccount;

/**
 * 
 * @author Thomas Dedek
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Entity
@Table(name = "ORDERS")
public class Order extends AbstractEntity<OrderIdentifier> implements Comparable<Order> {

	@EmbeddedId
	@AttributeOverride(name = "id", column = @Column(name = "ORDER_ID"))
	private OrderIdentifier orderIdentifier = new OrderIdentifier();

	@Lob
	private PaymentMethod paymentMethod;

	@OneToOne
	@AttributeOverride(name = "id", column = @Column(name = "OWNER_ID"))
	private UserAccount userAccount;

	@Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
	private LocalDateTime dateCreated = null;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus = OrderStatus.OPEN;

	@OneToMany(cascade = CascadeType.ALL)
	private Set<OrderLine> orderLines = new HashSet<OrderLine>();

	@OneToMany(cascade = CascadeType.ALL)
	private Set<ChargeLine> chargeLines = new HashSet<ChargeLine>();

	/**
	 * Parameterless constructor required for JPA. Do not use.
	 */
	@Deprecated
	protected Order() {
	}

	/**
	 * Creates a new Order
	 * 
	 * @param userAccount
	 *            The {@link UserAccount} connected to this
	 *            order
	 * @param paymentMethod
	 *            The {@link PaymentMethod} connected to this order
	 * @throws NullPointerException
	 *             if userAccount or paymentMethod are null
	 */
	public Order(UserAccount userAccount,
			PaymentMethod paymentMethod) {
		this.userAccount = Objects.requireNonNull(userAccount,
				"userAccount must not be null");
		this.paymentMethod = Objects.requireNonNull(paymentMethod,
				"paymentMethod must not be null");
	}

	/**
	 * Creates a new Order
	 * 
	 * @param userAccount
	 *            The {@link UserAccount} connected to this
	 *            order
	 * @throws NullPointerException
	 *             if userAccount is null
	 */
	public Order(UserAccount userAccount) {
		this.userAccount = Objects.requireNonNull(userAccount,
				"userAccount must not be null");
	}

	/**
	 * Adds an {@link OrderLine} to the order, the {@link OrderStatus} must be OPEN 
	 * @param orderLine the Orderline to be added
	 * @return true if the orderline was added, else false
	 * @throws NullPointerException if orderLine is null
	 */
	public boolean addOrderLine(OrderLine orderLine) {
		Objects.requireNonNull(orderLine, "orderLine must not be null");
		if (orderStatus != OrderStatus.OPEN) {
			return false;
		}
		return orderLines.add(orderLine);
	}

	public boolean removeOrderLine(OrderLineIdentifier orderLineIdentifier) {
		Objects.requireNonNull(orderLineIdentifier,
				"orderLineIdentifier must not be null");
		if (orderStatus != OrderStatus.OPEN) {
			return false;
		}
		OrderLine temp = null;
		for (OrderLine pol : orderLines) {
			if (pol.getIdentifier().equals(orderLineIdentifier)) {
				temp = pol;
				break;
			}
		}
		return orderLines.remove(temp);
	}

	public boolean addChargeLine(ChargeLine chargeLine) {
		Objects.requireNonNull(chargeLine, "chargeLine must not be null");
		if (orderStatus != OrderStatus.OPEN) {
			return false;
		}
		return chargeLines.add(chargeLine);
	}

	public boolean removeChargeLine(ChargeLineIdentifier chargeLineIdentifier) {
		Objects.requireNonNull(chargeLineIdentifier,
				"chargeLineIdentifier must not be null");

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

	public Iterable<OrderLine> getOrderLines() {
		return orderLines;
	}

	public Iterable<ChargeLine> getChargeLines() {
		return chargeLines;
	}

	public final OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public final UserAccount getUserAccount() {
		return userAccount;
	}

	/**
	 * @deprecated  As of release 5.3.1, replaced by {@link OrderManager#cancelOrder(Order)}
	 */
	@Deprecated
	public boolean cancelOrder() {
		if (orderStatus == OrderStatus.OPEN) {
			orderStatus = OrderStatus.CANCELLED;
			return true;
		} else {
			return false;
		}
	}

	public Money getTotalPrice() {
		return this.getOrderedLinesPrice().plus(this.getChargeLinesPrice());
	}

	public Money getOrderedLinesPrice() {
		Money price = Money.zero(CurrencyUnit.EUR);
		for (OrderLine orderLine : orderLines) {
			price = price.plus(orderLine.getPrice());
		}
		return price;
	}

	public Money getChargeLinesPrice() {
		Money price = Money.zero(CurrencyUnit.EUR);
		for (ChargeLine chargeLine : chargeLines) {
			price = price.plus(chargeLine.getPrice());
		}
		return price;
	}


	@Override
	public String toString() {
		return "User: " + userAccount.toString() + " | Order"
				+ orderIdentifier.toString();
	}

	public final LocalDateTime getDateCreated() {
		return dateCreated;
	}


	/*
	 * @Override public OrderCompletionResult completeOrder() {
	 * //System.out.println("---COMPLETE ORDER BEGIN");
	 * 
	 * // mehr CompletionStates? oder letzten Param (Exception) // überdenken,
	 * String cause? -> log4j nutzen!! if (orderStatus != OrderStatus.PAYED) {
	 * return new InternalOrderCompletionResult(OrderCompletionStatus.FAILED,
	 * null, null, null); }
	 * 
	 * Inventory<?> tempInventory = Shop.INSTANCE.getInventory();
	 * 
	 * if(tempInventory == null) { throw new
	 * NullPointerException("Shop.INSTANCE.getInventory() returned null"); }
	 * 
	 * if (!(tempInventory instanceof PersistentInventory)) { throw new
	 * RuntimeException
	 * ("Sorry, Order works only with PersistentInventory :("); }
	 * 
	 * EntityManager em =
	 * Database.INSTANCE.getEntityManagerFactory().createEntityManager();
	 * 
	 * PersistentInventory inventory = ((PersistentInventory)
	 * tempInventory).newInstance(em);
	 * 
	 * Set<OrderLine> remainingOrderLines = new
	 * HashSet<OrderLine>(this.orderLines);
	 * 
	 * List<ProductInstance> removedProducts = new
	 * LinkedList<ProductInstance>();
	 * 
	 * em.getTransaction().begin();
	 * 
	 * 
	 * //System.out.println("BEGIN ORDERLINE LOOP"); for (OrderLine
	 * orderLine : orderLines) { //System.out.println("remaining orderlines:" +
	 * remainingOrderLines.size()); remainingOrderLines.remove(orderLine);
	 * Iterable<PersistentProductInstance> tempProducts = null;
	 * //inventory.find(PersistentProductInstance.class,
	 * orderLine.getProductIdentifier(),orderLine.getProductFeatures());
	 * 
	 * List<PersistentProductInstance> products =
	 * Iterables.asList(tempProducts);
	 * 
	 * //System.out.println("products count:" + products.size());
	 * 
	 * int numberOrdered = 0; //orderLine.getQuantity();
	 * 
	 * //System.out.println("number ordered:" + numberOrdered);
	 * 
	 * int removed = 0;
	 * 
	 * //System.out.println("BEGIN PRODUCT LOOP"); for
	 * (PersistentProductInstance product : products) {
	 * //System.out.println("SerialNumber:" + product.getSerialNumber());
	 * boolean result = inventory.remove(product.getSerialNumber());
	 * //System.out.println("removal result:" + result);
	 * 
	 * if (!result) { //System.out.println("not removed");
	 * 
	 * 
	 * removedProducts.add(product);
	 * 
	 * Order order = new Order(this.userIdentifier,
	 * this.paymentMethod); OrderLine pol = new
	 * OrderLine(orderLine.getProductIdentifier(),
	 * orderLine.getProductFeatures(), numberOrdered - removed);
	 * order.orderLines.add(pol); order.orderLines.addAll(remainingOrderLines);
	 * order.chargeLines.addAll(this.chargeLines); order.orderStatus =
	 * OrderStatus.PAYED;
	 * 
	 * return new
	 * InternalOrderCompletionResult(OrderCompletionStatus.SPLITORDER, order,
	 * em, removedProducts); } else { System.out.println("removed++");
	 * removed++; }
	 * 
	 * if (removed >= numberOrdered) {
	 * //System.out.println("removed >= numberordered");
	 * //System.out.println(removed + ">=" + numberOrdered); break; } } }
	 * 
	 * try { em.getTransaction().commit(); } catch (RollbackException e) { //
	 * "RollbackException - if the commit fails" return new
	 * InternalOrderCompletionResult(OrderCompletionStatus.FAILED, null, em, new
	 * LinkedList<ProductInstance>()); }
	 * 
	 * //System.out.println("----COMPLETE ORDER END"); this.orderStatus =
	 * OrderStatus.COMPLETED; return new
	 * InternalOrderCompletionResult(OrderCompletionStatus.SUCCESSFUL, null, em,
	 * removedProducts); }
	 */

	/**
	 * Convenience method for checking if an order has the status PAYED
	 * 
	 * @return true if OrderStatus is PAYED, otherwise false
	 */

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
	public int compareTo(Order other) {
		return this.orderIdentifier.compareTo(other.orderIdentifier);
	}

	public final OrderIdentifier getIdentifier() {
		return orderIdentifier;
	}

	public final PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		if (orderStatus != OrderStatus.OPEN)
			return;
		this.paymentMethod = Objects.requireNonNull(paymentMethod,
				"paymentMethod must not be null");
	}
	
	void complete() {
		this.orderStatus = OrderStatus.COMPLETED;
	}
	
	void cancel() {
		this.orderStatus = OrderStatus.CANCELLED;
	}
	
	
	int getNumberOfLineItems() {
		return this.orderLines.size();
	}
	
	boolean isPaymentExpected() {
		return orderStatus == OrderStatus.OPEN && paymentMethod != null;
	}
	
	ProductPaymentEntry markPaid() {
	
			ProductPaymentEntry ppe = new ProductPaymentEntry(
					this.orderIdentifier, this.userAccount,
					this.getTotalPrice(), "Rechnung Nr. " + this.orderIdentifier,
					this.paymentMethod);
			orderStatus = OrderStatus.PAYED;
			
			return ppe;
	}

	void setDateCreated(LocalDateTime dateTime) {
		this.dateCreated = dateTime;
	}
}
