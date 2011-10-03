package org.salespointframework.core.order;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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

import org.joda.time.DateTime;
import org.salespointframework.core.accountancy.PersistentAccountancy;
import org.salespointframework.core.accountancy.ProductPaymentEntry;
import org.salespointframework.core.accountancy.payment.Payment;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.inventory.Inventory;
import org.salespointframework.core.inventory.PersistentInventory;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.order.OrderCompletionResult.OrderCompletionStatus;
import org.salespointframework.core.product.PersistentProduct;
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
public class PersistentOrder implements
		Order<PersistentOrderLine, PersistentChargeLine>,
		Comparable<PersistentOrder> {
	// TODO: Here, we also need to rename the column, or OWNER_ID will be the
	// PK. Maybe we should rename the field in SalespointIdentifier, to avoid
	// name clashes with "ID"?
	@EmbeddedId
	@AttributeOverride(name = "id", column = @Column(name = "ORDER_ID"))
	private OrderIdentifier orderIdentifier = new OrderIdentifier();

	private Payment payment;

	@Embedded
	@AttributeOverride(name = "id", column = @Column(name = "OWNER_ID"))
	private UserIdentifier userIdentifier;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated = Shop.INSTANCE.getTime().getDateTime().toDate();

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus = OrderStatus.OPEN;

	@OneToMany(cascade = CascadeType.ALL)
	private Set<PersistentOrderLine> orderLines = new HashSet<PersistentOrderLine>();

	@OneToMany(cascade = CascadeType.ALL)
	private Set<PersistentChargeLine> chargeLines = new HashSet<PersistentChargeLine>();

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
	 * @param payment
	 *            The {@link Payment} connected to this order
	 */
	public PersistentOrder(UserIdentifier userIdentifier, Payment payment) {
		this.userIdentifier = Objects.requireNonNull(userIdentifier,
				"userIdentifier");
		this.payment = Objects.requireNonNull(payment, "payment");
	}
	
	// TODO wegen payment, fliegt später raus
	@Deprecated
	public PersistentOrder(UserIdentifier userIdentifier) {
		this.userIdentifier = Objects.requireNonNull(userIdentifier,
				"userIdentifier");
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
	public final boolean addChargeLine(PersistentChargeLine chargeLine) {
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
		PersistentChargeLine temp = null;
		for (PersistentChargeLine pcl : chargeLines) {
			if (pcl.getIdentifier().equals(chargeLineIdentifier)) {
				temp = pcl;
				break;
			}
		}
		return chargeLines.remove(temp);
	}

	@Override
	public final Iterable<PersistentOrderLine> getOrderLines() {
		return Iterables.from(orderLines);
	}

	@Override
	public final Iterable<PersistentChargeLine> getChargeLines() {
		return Iterables.from(chargeLines);
	}

	@Override
	public final OrderStatus getOrderStatus() {
		return orderStatus;
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
		for (ChargeLine chargeLine : chargeLines) {
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
		// TODO mehr CompletionStates? oder letzten Param (Exception)
		// überdenken, String cause?
		if (orderStatus != OrderStatus.PAYED) {
			return new InternalOrderCompletionResult(
					OrderCompletionStatus.FAILED, null, null, null);
		}

		Inventory<?> tempInventory = Shop.INSTANCE.getInventory();
		if (!(tempInventory instanceof PersistentInventory)) {
			// TODO Exception Name
			throw new RuntimeException("Sorry, PersistentInventory only :(");
		}

		EntityManager em = Database.INSTANCE.getEntityManagerFactory()
				.createEntityManager();

		PersistentInventory inventory = ((PersistentInventory) tempInventory)
				.newInstance(em);
		Set<PersistentOrderLine> remainingOrderLines = new HashSet<PersistentOrderLine>(
				this.orderLines);

		em.getTransaction().begin();

		for (PersistentOrderLine orderLine : orderLines) {
			remainingOrderLines.remove(orderLine);
			Iterable<PersistentProduct> tempProducts = inventory.find(
					PersistentProduct.class, orderLine.getProductIdentifier(),
					orderLine.getProductFeatures());

			List<PersistentProduct> products = Iterables.toList(tempProducts);

			int numberOrdered = orderLine.getNumberOrdered();
			int removed = 0;

			for (PersistentProduct product : products) {
				boolean result = inventory.remove(product.getSerialNumber());

				if (!result) {
					// TODO payment clone?
					PersistentOrder order = new PersistentOrder(
							this.userIdentifier, this.payment);
					PersistentOrderLine pol = new PersistentOrderLine(
							orderLine.getProductIdentifier(),
							orderLine.getProductFeatures(), numberOrdered
									- removed);
					order.orderLines.add(pol);
					order.orderLines.addAll(remainingOrderLines);
					order.chargeLines.addAll(this.chargeLines);
					order.orderStatus = OrderStatus.PAYED;

					return new InternalOrderCompletionResult(
							OrderCompletionStatus.SPLITORDER, order, em, null);

				} else {
					removed++;
				}

				if (removed >= numberOrdered) {
					break;
				}
			}
		}

		try {
			em.getTransaction().commit();
		} catch (RollbackException e) { // "RollbackException - if the commit fails"
			return new InternalOrderCompletionResult(
					OrderCompletionStatus.FAILED, null, em, e.getCause());
		}

		return new InternalOrderCompletionResult(
				OrderCompletionStatus.SUCCESSFUL, null, em, null);
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
		if (!(orderStatus == OrderStatus.PAYED)) {
			return false;
		}
		;
		// TODO payment nutzen

		ProductPaymentEntry ppe = new ProductPaymentEntry(this.orderIdentifier,
				this.userIdentifier, this.getTotalPrice(),
				"a nice string goes here");
		PersistentAccountancy pA = (PersistentAccountancy) Shop.INSTANCE
				.getAccountancy(); // TODO not only Persistent?
		pA.add(ppe);
		orderStatus = OrderStatus.PAYED;
		return true;
	}

	// gotta love inner classes
	private final class InternalOrderCompletionResult implements
			OrderCompletionResult {

		private final OrderCompletionStatus orderCompletionStatus;
		private final PersistentOrder order;
		private final EntityManager entityManager;
		private Throwable exception;

		public InternalOrderCompletionResult(
				OrderCompletionStatus orderCompletionStatus,
				PersistentOrder order, EntityManager entityManager,
				Throwable exception) {
			this.orderCompletionStatus = orderCompletionStatus;
			this.order = order;
			this.entityManager = entityManager;
			this.exception = exception;
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
						this.exception = e.getCause();
						return null;
					}
				}
				return order;
			}
			return null;
		}

		@Override
		public final boolean rollBack() {
			if (this.orderCompletionStatus == OrderCompletionStatus.SPLITORDER) {
				if (this.entityManager.getTransaction().isActive()) {
					this.entityManager.getTransaction().rollback();
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
		public Throwable getException() {
			return this.exception;
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
