package org.salespointframework.core.order;

import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.joda.time.DateTime;
import org.salespointframework.core.accountancy.ProductPaymentEntry;
import org.salespointframework.core.accountancy.payment.PaymentMethod;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.useraccount.UserAccount;
import org.salespointframework.core.useraccount.UserAccountIdentifier;
import org.salespointframework.util.Iterables;

/**
 * 
 * @author Thomas Dedek
 * @author Paul Henke
 * 
 */
@Entity
@Table(name = "ORDERS")
public class Order implements Comparable<Order> {

	@EmbeddedId
	@AttributeOverride(name = "id", column = @Column(name = "ORDER_ID"))
	private OrderIdentifier orderIdentifier = new OrderIdentifier();

	@Lob
	private PaymentMethod paymentMethod;

	@OneToOne
	@AttributeOverride(name = "id", column = @Column(name = "OWNER_ID"))
	private UserAccount userAccount;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated = null;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus = OrderStatus.OPEN;

	@OneToMany(cascade = CascadeType.ALL)
	private Set<OrderLine> orderLines = new HashSet<OrderLine>();

	@OneToMany(cascade = CascadeType.ALL)
	//@CollectionTable(joinColumns = @JoinColumn(name = "order_id"))
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
	 *            The {@link UserAccountIdentifier}/{@link UserAccount} connected to this
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
	 *            The {@link UserAccountIdentifier}/{@link UserAccount} connected to this
	 *            order
	 * @throws NullPointerException
	 *             if userAccount is null
	 */
	public Order(UserAccount userAccount) {
		this.userAccount = Objects.requireNonNull(userAccount,
				"userAccount must not be null");
	}

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
		return Iterables.of(orderLines);
	}

	public Iterable<ChargeLine> getChargeLines() {
		return Iterables.of(chargeLines);
	}

	public final OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public final UserAccount getUserAccount() {
		return userAccount;
	}

	public boolean cancelOrder() {
		if (orderStatus == OrderStatus.OPEN) {
			orderStatus = OrderStatus.CANCELLED;
			return true;
		} else {
			return false;
		}
	}

	public Money getTotalPrice() {
		return this.getOrderedLinesPrice().add(this.getChargeLinesPrice());
	}

	public Money getOrderedLinesPrice() {
		Money price = Money.ZERO;
		for (OrderLine orderLine : orderLines) {
			price = price.add(orderLine.getPrice());
		}
		return price;
	}

	public Money getChargeLinesPrice() {
		Money price = Money.ZERO;
		for (ChargeLine chargeLine : chargeLines) {
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
		if (other instanceof Order) {
			return this.orderIdentifier
					.equals(((Order) other).orderIdentifier);
		}
		return false;
	}

	@Override
	public final int hashCode() {
		return this.orderIdentifier.hashCode();
	}

	@Override
	public String toString() {
		return "User: " + userAccount.toString() + " | Order"
				+ orderIdentifier.toString();
	}

	public final DateTime getDateCreated() {
		return new DateTime(dateCreated);
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

	

	/*
	 * private final class InternalOrderCompletionResult implements
	 * OrderCompletionResult {
	 * 
	 * private final OrderCompletionStatus orderCompletionStatus; private final
	 * Order order; private final List<ProductInstance>
	 * removedProducts; private final EntityManager entityManager;
	 * 
	 * public InternalOrderCompletionResult( OrderCompletionStatus
	 * orderCompletionStatus, Order order, EntityManager
	 * entityManager, List<ProductInstance> removedProducts) {
	 * this.orderCompletionStatus = orderCompletionStatus; this.order = order;
	 * this.removedProducts = removedProducts; this.entityManager =
	 * entityManager; }
	 * 
	 * @SuppressWarnings({ "unchecked", "rawtypes" })
	 * 
	 * @Override public final Order splitOrder() { if
	 * (this.orderCompletionStatus == OrderCompletionStatus.SPLITORDER) { if
	 * (this.entityManager.getTransaction().isActive()) { try { // Commit Fails
	 * -> Auto Rollback? this.entityManager.getTransaction().commit(); } catch
	 * (RollbackException e) { return null; } } orderStatus =
	 * OrderStatus.COMPLETED; return order; } return null; }
	 * 
	 * @Override public final boolean rollBack() { if
	 * (this.orderCompletionStatus == OrderCompletionStatus.SPLITORDER) { if
	 * (this.entityManager.getTransaction().isActive()) {
	 * this.entityManager.getTransaction().rollback(); removedProducts.clear();
	 * return true; } else { return false; } } else { return false; } }
	 * 
	 * @Override public final OrderCompletionStatus getStatus() { return
	 * this.orderCompletionStatus; }
	 * 
	 * @Override public Iterable<ProductInstance> getRemovedInstances() { return
	 * Iterables.of(removedProducts); } }
	 */

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

	void setDateCreated(DateTime dateTime) {
		this.dateCreated = dateTime.toDate();
	}
}
