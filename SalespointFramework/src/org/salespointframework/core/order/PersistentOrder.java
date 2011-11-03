package org.salespointframework.core.order;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.RollbackException;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.joda.time.DateTime;
import org.salespointframework.core.accountancy.PersistentAccountancy;
import org.salespointframework.core.accountancy.ProductPaymentEntry;
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
import org.salespointframework.util.Objects;

/**
 * 
 * @author Thomas Dedek
 * @author Paul Henke
 * 
 */
@Entity
public class PersistentOrder implements Order<PersistentOrderLine>, Comparable<PersistentOrder> {
	// TODO: Here, we also need to rename the column, or OWNER_ID will be the
	// PK. Maybe we should rename the field in SalespointIdentifier, to avoid
	// name clashes with "ID"?
	@EmbeddedId
	@AttributeOverride(name = "id", column = @Column(name = "ORDER_ID"))
	private OrderIdentifier orderIdentifier = new OrderIdentifier();

	private PaymentMethod paymentMethod;

	@Embedded
	@AttributeOverride(name = "id", column = @Column(name = "OWNER_ID"))
	private UserIdentifier userIdentifier;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated = Shop.INSTANCE.getTime().getDateTime().toDate();

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus = OrderStatus.OPEN;

	@OneToMany(cascade = CascadeType.ALL)
	private Set<PersistentOrderLine> orderLines = new HashSet<PersistentOrderLine>();

	@ElementCollection
	private Set<ChargeLine> chargeLines = new HashSet<ChargeLine>();

	/**
	 * Parameterless constructor required for JPA. Do not use.
	 */
	@Deprecated
	public PersistentOrder() {
	}

	/**
	 * Creates a new PersistentOrder
	 * 
	 * @param userIdentifier
	 *            The {@link UserIdentifier}/{@link User} connected to this
	 *            order
	 * @param paymentMethod
	 *            The {@link PaymentMethod} connected to this order
	 */
	public PersistentOrder(UserIdentifier userIdentifier, PaymentMethod paymentMethod) {
		this.userIdentifier = Objects.requireNonNull(userIdentifier, "userIdentifier");
		this.paymentMethod = Objects.requireNonNull(paymentMethod, "paymentMethod");
	}
	
	@Override
	public final boolean addOrderLine(PersistentOrderLine orderLine) {
		Objects.requireNonNull(orderLine, "orderLine");
		if (orderStatus != OrderStatus.OPEN) {
			return false;
		}
		return orderLines.add(orderLine);
	}

	@Override
	public final boolean removeOrderLine(OrderLineIdentifier orderLineIdentifier) {
		Objects.requireNonNull(orderLineIdentifier, "orderLineIdentifier");
		if (orderStatus != OrderStatus.OPEN) {
			return false;
		}
		PersistentOrderLine temp = null;
		for (PersistentOrderLine pol : orderLines) {
			if (pol.getIdentifier().equals(orderLineIdentifier)) {
				temp = pol;
				break;
			}
		}
		return orderLines.remove(temp);
	}

	@Override
	public final boolean addChargeLine(ChargeLine chargeLine) {
		Objects.requireNonNull(chargeLine, "chargeLine");
		if (orderStatus != OrderStatus.OPEN) {
			return false;
		}
		return chargeLines.add(chargeLine);
	}

	@Override
	public final boolean removeChargeLine(ChargeLineIdentifier chargeLineIdentifier) {
		Objects.requireNonNull(chargeLineIdentifier, "chargeLineIdentifier");
		
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
	public final Iterable<PersistentOrderLine> getOrderLines() {
		return Iterables.of(orderLines);
	}

	@Override
	public final Iterable<ChargeLine> getChargeLines() {
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
	public final boolean cancelOrder() {
		if (orderStatus == OrderStatus.OPEN) {
			orderStatus = OrderStatus.CANCELLED;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public final Money getTotalPrice() {
		return this.getOrderedLinesPrice().add(this.getChargeLinesPrice());
	}

	@Override
	public final Money getOrderedLinesPrice() {
		Money price = Money.ZERO;
		for (OrderLine orderLine : orderLines) {
			price = price.add(orderLine.getPrice());
		}
		return price;
	}

	@Override
	public final Money getChargeLinesPrice() {
		Money price = Money.ZERO;
		for(ChargeLine chargeLine : chargeLines) {
			price = price.add(chargeLine.getPrice());
		}
		return price;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (other == this) {
			return true;
		}
		if (other instanceof PersistentOrder) {
			return this.orderIdentifier
					.equals(((PersistentOrder) other).orderIdentifier);
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

	@Override
	public final OrderCompletionResult completeOrder() {
		//System.out.println("---COMPLETE ORDER BEGIN");
		
		// TODO mehr CompletionStates? oder letzten Param (Exception)
		// überdenken, String cause? -> log4j nutzen!!
		if (orderStatus != OrderStatus.PAYED) {
			return new InternalOrderCompletionResult(OrderCompletionStatus.FAILED, null, null, null);
		}

		Inventory<?> tempInventory = Shop.INSTANCE.getInventory();
		if (!(tempInventory instanceof PersistentInventory)) {
			// TODO which Exception
			throw new RuntimeException("Sorry, PersistentInventory only :(");
		}

		EntityManager em = Database.INSTANCE.getEntityManagerFactory().createEntityManager();

		PersistentInventory inventory = ((PersistentInventory) tempInventory).newInstance(em);
		
		Set<PersistentOrderLine> remainingOrderLines = new HashSet<PersistentOrderLine>(this.orderLines);
		
		List<ProductInstance> removedProducts = new LinkedList<ProductInstance>();
		
		em.getTransaction().begin();
		

		//System.out.println("BEGIN ORDERLINE LOOP");
		for (PersistentOrderLine orderLine : orderLines) {
			//System.out.println("remaining orderlines:" + remainingOrderLines.size());
			remainingOrderLines.remove(orderLine);
			Iterable<PersistentProductInstance> tempProducts = inventory.find(PersistentProductInstance.class, orderLine.getProductIdentifier(),orderLine.getProductFeatures());

			List<PersistentProductInstance> products = Iterables.asList(tempProducts);
			
			//System.out.println("products count:" + products.size());

			int numberOrdered = orderLine.getNumberOrdered();
			
			//System.out.println("number ordered:" + numberOrdered);
			
			int removed = 0;

			//System.out.println("BEGIN PRODUCT LOOP");
			for (PersistentProductInstance product : products) {
				//System.out.println("SerialNumber:" + product.getSerialNumber());
				boolean result = inventory.remove(product.getSerialNumber());
				//System.out.println("removal result:" + result);

				if (!result) {
					//System.out.println("not removed");
					// TODO payment clone?
					
					removedProducts.add(product);
					
					PersistentOrder order = new PersistentOrder(this.userIdentifier, this.paymentMethod);
					PersistentOrderLine pol = new PersistentOrderLine(orderLine.getProductIdentifier(),	orderLine.getProductFeatures(), numberOrdered - removed);
					order.orderLines.add(pol);
					order.orderLines.addAll(remainingOrderLines);
					order.chargeLines.addAll(this.chargeLines);
					order.orderStatus = OrderStatus.PAYED;

					return new InternalOrderCompletionResult(OrderCompletionStatus.SPLITORDER, order, em, removedProducts);
				} else {
					System.out.println("removed++");
					removed++;
				}

				if (removed >= numberOrdered) {
					//System.out.println("removed >= numberordered");
					//System.out.println(removed + ">=" + numberOrdered);
					break;
				}
			}
		}

		try {
			em.getTransaction().commit();
		} catch (RollbackException e) { // "RollbackException - if the commit fails"
			return new InternalOrderCompletionResult(OrderCompletionStatus.FAILED, null, em, new LinkedList<ProductInstance>());
		}

		//System.out.println("----COMPLETE ORDER END");
		this.orderStatus = OrderStatus.COMPLETED;
		return new InternalOrderCompletionResult(OrderCompletionStatus.SUCCESSFUL, null, em, removedProducts);
	}

	/**
	 * Convenience method for checking if an order has the status PAYED
	 * 
	 * @return true if OrderStatus is PAYED, otherwise false
	 */

	public final boolean isPayed() {
		return orderStatus == OrderStatus.PAYED;
	}

	/**
	 * Convenience method for checking if an order has the status CANCELLED
	 * 
	 * @return true if OrderStatus is CANCELLED, otherwise false
	 */
	public final boolean isCanceled() {
		return orderStatus == OrderStatus.CANCELLED;
	}

	/**
	 * Convenience method for checking if an order has the status COMPLETED
	 * 
	 * @return true if OrderStatus is COMPLETED, otherwise false
	 */
	public final boolean isCompleted() {
		return orderStatus == OrderStatus.COMPLETED;
	}

	/**
	 * Convenience method for checking if an order has the status OPEN
	 * 
	 * @return true if OrderStatus is OPEN, otherwise false
	 */
	public final boolean isOpen() {
		return orderStatus == OrderStatus.OPEN;
	}

	@Override
	public boolean payOrder() {
		if (orderStatus != OrderStatus.OPEN) {
			return false;
		}
		;

		// TODO "Rechnung Nr " deutsch?
		ProductPaymentEntry ppe = new ProductPaymentEntry(this.orderIdentifier,	this.userIdentifier, this.getTotalPrice(), "Rechnung Nr. " + this.orderIdentifier, this.paymentMethod);
		PersistentAccountancy pA = (PersistentAccountancy) Shop.INSTANCE.getAccountancy(); // TODO not only Persistent?
		pA.add(ppe);
		orderStatus = OrderStatus.PAYED;
		return true;
	}

	// gotta love inner classes
	private final class InternalOrderCompletionResult implements OrderCompletionResult {

		private final OrderCompletionStatus orderCompletionStatus;
		private final PersistentOrder order;
		private final List<ProductInstance> removedProducts;
		private final EntityManager entityManager;

		public InternalOrderCompletionResult(
				OrderCompletionStatus orderCompletionStatus,
				PersistentOrder order, EntityManager entityManager,
				List<ProductInstance> removedProducts) {
			this.orderCompletionStatus = orderCompletionStatus;
			this.order = order;
			this.removedProducts = removedProducts;
			this.entityManager = entityManager;
		}

		// TODO SupressWarnings
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public final Order splitOrder() {
			if (this.orderCompletionStatus == OrderCompletionStatus.SPLITORDER) {
				if (this.entityManager.getTransaction().isActive()) {
					try {
						// TODO Commit Fails -> Auto Rollback?
						this.entityManager.getTransaction().commit();
					} catch (RollbackException e) {
						return null;
					}
				}
				orderStatus = OrderStatus.COMPLETED;
				return order;
			}
			return null;
		}

		@Override
		public final boolean rollBack() {
			if (this.orderCompletionStatus == OrderCompletionStatus.SPLITORDER) {
				if (this.entityManager.getTransaction().isActive()) {
					this.entityManager.getTransaction().rollback();
					removedProducts.clear();
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}

		@Override
		public final OrderCompletionStatus getStatus() {
			return this.orderCompletionStatus;
		}

		@Override
		public Iterable<ProductInstance> getRemovedInstances() {
			return Iterables.of(removedProducts);
		}
	}

	@Override
	public int compareTo(PersistentOrder other) {
		return this.orderIdentifier.compareTo(other.orderIdentifier);
	}

	@Override
	public OrderIdentifier getIdentifier() {
		return orderIdentifier;
	}
}
