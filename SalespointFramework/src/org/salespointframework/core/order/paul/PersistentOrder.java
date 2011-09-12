package org.salespointframework.core.order.paul;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.DateTime;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.order.OrderIdentifier;
import org.salespointframework.core.order.OrderLineIdentifier;
import org.salespointframework.core.order.OrderLogEntry;
import org.salespointframework.core.order.OrderStatus;
import org.salespointframework.core.shop.Shop;
import org.salespointframework.core.users.UserIdentifier;
import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;

/**
 * 
 * @author Thomas Dedek
 * @author Paul Henke
 *
 */
@Entity
public class PersistentOrder implements Order<PersistentOrderLine, PersistentChargeLine> {

	@EmbeddedId
	private OrderIdentifier orderIdentifier;
	
	@Embedded
	@AttributeOverride(name = "id", column = @Column(name = "OWNER_ID"))
	private UserIdentifier userIdentifier;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated;
	
	private String salesChannel;
	private String termsAndConditions;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	
	@OneToMany(cascade = CascadeType.ALL)
	private Set<PersistentOrderLine> orderLines = new HashSet<PersistentOrderLine>();
	
	@OneToMany(cascade = CascadeType.ALL)
	private Set<PersistentChargeLine> chargeLines = new HashSet<PersistentChargeLine>();

	@ElementCollection
	private List<OrderLogEntry> log = new ArrayList<OrderLogEntry>();

	@Deprecated
	public PersistentOrder() {
	}
	
	public PersistentOrder(UserIdentifier userIdentifier, String salesChannel) {
		this(userIdentifier, salesChannel, "");
	}
		
	public PersistentOrder(UserIdentifier userIdentifier, String salesChannel, String termsAndConditions) {
		this.salesChannel = Objects.requireNonNull(salesChannel, "salesChannel");
		this.termsAndConditions = Objects.requireNonNull(termsAndConditions, "termsAndConditions");
		this.userIdentifier = Objects.requireNonNull(userIdentifier,"userIdentifier");

		orderIdentifier = new OrderIdentifier();
		dateCreated = Shop.INSTANCE.getTime().getDateTime().toDate();
		orderStatus = OrderStatus.OPEN;
		this.log.add(new OrderLogEntry("OrderEntry initialized succesfully", ""));
	}
	
	
	// TODO
	@Override
	public OrderCompletionResult completeOrder() {
		return new OrderCompletionResultImpl(null, null, null);
	}
	
	@Override
	public boolean addOrderLine(PersistentOrderLine orderLine) {
		return orderLines.add(orderLine);
	}

	@Override
	public boolean removeOrderLine(OrderLineIdentifier orderLineIdentifier) {
		PersistentOrderLine temp = null;
		for(PersistentOrderLine pol : orderLines) {
			if(pol.getOrderLineIdentifier().equals(orderLineIdentifier)) {
				temp = pol;
				break;
			}
		}
		return orderLines.remove(temp);
	}

	@Override
	public boolean addChargeLine(PersistentChargeLine chargeLine) {
		return chargeLines.add(chargeLine);
		
	}

	@Override
	public boolean removeChargeLine(ChargeLineIdentifier chargeLineIdentifier) {
		PersistentChargeLine temp = null;
		for(PersistentChargeLine pcl : chargeLines) {
			if(pcl.getChargeLineIdentifier().equals(chargeLineIdentifier)) {
				temp = pcl;
				break;
			}
		}
		return chargeLines.remove(temp);
		
	}


	@Override
	public String getTermsAndConditions() {
		return termsAndConditions;
	}

	@Override
	public Iterable<PersistentOrderLine> getOrderLines() {
		return Iterables.from(orderLines);
	}

	@Override
	public Iterable<PersistentChargeLine> getChargeLines() {
		return Iterables.from(chargeLines);
	}

	@Override
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	@Override
	public void cancelOrder() {
		orderStatus = OrderStatus.CANCELLED;
	}

	@Override
	public Iterable<OrderLogEntry> getLogEntries() {
		return Iterables.from(log);
	}

	@Override
	public Money getTotalPrice() {
		return this.getOrderedLinesPrice().add_(this.getChargeLinesPrice());
	}

	@Override
	public Money getOrderedLinesPrice() {
		Money price = Money.ZERO;
		for(OrderLine orderLine : orderLines) {
			price = price.add_(orderLine.getPrice());
		}
		return price;
	}

	@Override
	public Money getChargeLinesPrice() {
		Money price = Money.ZERO;
		for(ChargeLine chargeLine : chargeLines) {
			price = price.add_(chargeLine.getPrice());
		}
		return price;
		
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null)	return false;
		if (other == this)	return true;
		if (!(other instanceof PersistentOrder)) return false;
		return this.equals((PersistentOrder) other);
	}

	public final boolean equals(PersistentOrder other) {
		if (other == this) return true;
		if (other == null) return false;
		return this.orderIdentifier.equals(other.orderIdentifier);
	}

	@Override
	public final int hashCode() {
		return this.orderIdentifier.hashCode();
	}
	
	// TODO
	@Override
	public String toString() {
		return "TODO";
	}

	@Override
	public DateTime getCreationDate() {
		return new DateTime(dateCreated);
	}
	
	private class OrderCompletionResultImpl implements OrderCompletionResult {

		private final OrderCompletionStatus orderCompletionStatus;
		private final PersistentOrder order;
		private final EntityManager entityManager;

		public OrderCompletionResultImpl(OrderCompletionStatus orderCompletionStatus, PersistentOrder order, EntityManager entityManager) {
			this.orderCompletionStatus = orderCompletionStatus;
			this.order = order;
			this.entityManager = entityManager;
		}
		
		// TODO
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Order splitOrder() {
			if(this.orderCompletionStatus == OrderCompletionStatus.SUCCESSFUL) return null;
			
			PersistentOrder order = new PersistentOrder(this.order.userIdentifier, this.order.salesChannel, this.order.termsAndConditions);
			order.orderStatus = OrderStatus.PROCESSING;
			return order;
		}

		public void rollBack() {
			if(this.orderCompletionStatus == OrderCompletionStatus.SUCCESSFUL) return;
			this.entityManager.getTransaction().rollback();
		}

		@Override
		public OrderCompletionStatus getOrderCompletionStatus() {
			return this.orderCompletionStatus;
		}
	}

	
	/*
	@SuppressWarnings("unused")	
	@PostLoad
	private void postLoad() {
		emf = Database.INSTANCE.getEntityManagerFactory();
	}
	*/
}
