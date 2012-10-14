package org.salespointframework.core.order;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
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
import org.salespointframework.core.accountancy.Accountancy;
import org.salespointframework.core.accountancy.PersistentAccountancy;
import org.salespointframework.core.accountancy.PersistentProductPaymentEntry;
import org.salespointframework.core.accountancy.TransientAccountancy;
import org.salespointframework.core.accountancy.payment.PaymentMethod;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.inventory.Inventory;
import org.salespointframework.core.inventory.PersistentInventory;
import org.salespointframework.core.inventory.PersistentInventoryItem;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.order.OrderCompletionResult.OrderCompletionStatus;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.quantity.Quantity;
import org.salespointframework.core.shop.Shop;
import org.salespointframework.core.user.User;
import org.salespointframework.core.user.UserIdentifier;
import org.salespointframework.util.Iterables;

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
	protected PersistentOrder() {}

	/**
	 * Creates a new PersistentOrder
	 * 
	 * @param userIdentifier
	 *            The {@link UserIdentifier}/{@link User} connected to this
	 *            order
	 * @param paymentMethod
	 *            The {@link PaymentMethod} connected to this order
	 * @throws NullPointerException if userIdentifier or paymentMethod is null
	 */
	public PersistentOrder(UserIdentifier userIdentifier, PaymentMethod paymentMethod) {
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
	public PersistentOrder(UserIdentifier userIdentifier) {
		this.userIdentifier = Objects.requireNonNull(userIdentifier, "userIdentifier must not be null");
	}
	
	
	
	
	
	@Override
	public boolean addOrderLine(PersistentOrderLine orderLine) {
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
	public Iterable<PersistentOrderLine> getOrderLines() {
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
		if (other instanceof PersistentOrder) {
			return this.orderIdentifier.equals(((PersistentOrder) other).orderIdentifier);
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
	public OrderCompletionResult completeOrder() {
		
		Inventory<?> temp = Shop.INSTANCE.getInventory();
		
		if(temp == null) 
		{
			throw new NullPointerException("Shop.INSTANCE.getInventory() returned null");
		}
		
		if (!(temp instanceof PersistentInventory)) 
		{
			throw new RuntimeException("Sorry, PersistentOrder works only with PersistentInventory :(");
		}
		
		EntityManager em = Database.INSTANCE.getEntityManagerFactory().createEntityManager();
		
		PersistentInventory inventory = ((PersistentInventory) temp).newInstance(em);
		
		Map<PersistentInventoryItem, Quantity> goodItems = new HashMap<>();
		Map<PersistentInventoryItem, Quantity> badItems = new HashMap<>();
		
		for(PersistentOrderLine orderline : orderLines) {
			ProductIdentifier productIdentifier =  orderline.getProductIdentifier();
			PersistentInventoryItem inventoryItem = inventory.getByProductIdentifier(PersistentInventoryItem.class, productIdentifier); 
			
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
		
		boolean failed = false;
		
		em.getTransaction().begin();
		if(goodItems.size() == orderLines.size()) {
			System.out.println("size == ");
			for(PersistentInventoryItem inventoryItem : goodItems.keySet()) 
			{
				Quantity quantity = goodItems.get(inventoryItem);
				inventoryItem.decreaseQuantity(quantity);
				if(inventoryItem.getQuantity().isNegative()) 
				{
					failed = true; 
					break;
				}
			}
			
			// TODO DRY IT
			if(failed) 
			{
				em.getTransaction().rollback();
				return new InternalOrderCompletionResult(OrderCompletionStatus.FAILED);
			}
			try {
			em.getTransaction().commit();
			this.orderStatus = OrderStatus.COMPLETED;
			return new InternalOrderCompletionResult(OrderCompletionStatus.SUCCESSFUL);
			} catch (Exception ex) {
				em.getTransaction().rollback();
				return new InternalOrderCompletionResult(OrderCompletionStatus.FAILED);
			}
		} else {
			em.getTransaction().rollback();
			System.out.println("size != ");
			return new InternalOrderCompletionResult(OrderCompletionStatus.FAILED);
		}
	}
	
	/*
	@Override
	public OrderCompletionResult completeOrder() {
		//System.out.println("---COMPLETE ORDER BEGIN");
		
		// mehr CompletionStates? oder letzten Param (Exception)
		// überdenken, String cause? -> log4j nutzen!!
		if (orderStatus != OrderStatus.PAYED) {
			return new InternalOrderCompletionResult(OrderCompletionStatus.FAILED, null, null, null);
		}

		Inventory<?> tempInventory = Shop.INSTANCE.getInventory();
		
		if(tempInventory == null) {
			throw new NullPointerException("Shop.INSTANCE.getInventory() returned null");
		}
		
		if (!(tempInventory instanceof PersistentInventory)) {
			throw new RuntimeException("Sorry, PersistentOrder works only with PersistentInventory :(");
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
			Iterable<PersistentProductInstance> tempProducts = null; //inventory.find(PersistentProductInstance.class, orderLine.getProductIdentifier(),orderLine.getProductFeatures());

			List<PersistentProductInstance> products = Iterables.asList(tempProducts);
			
			//System.out.println("products count:" + products.size());

			int numberOrdered = 0; //orderLine.getQuantity();
			
			//System.out.println("number ordered:" + numberOrdered);
			
			int removed = 0;

			//System.out.println("BEGIN PRODUCT LOOP");
			for (PersistentProductInstance product : products) {
				//System.out.println("SerialNumber:" + product.getSerialNumber());
				boolean result = inventory.remove(product.getSerialNumber());
				//System.out.println("removal result:" + result);

				if (!result) {
					//System.out.println("not removed");

					
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
	public boolean payOrder() {
		if (orderStatus != OrderStatus.OPEN || paymentMethod == null) {
			return false;
		}
		
		Accountancy<?> temp = Shop.INSTANCE.getAccountancy();

		if(temp == null) 
		{
			throw new NullPointerException("Shop.INSTANCE.getAccountancy() returned null");
		}
		
		if(!(temp instanceof PersistentAccountancy)) 
		{
			throw new RuntimeException("Sorry, PersistentOrder works only with PersistentAccountancy :(");
		}
		
		PersistentAccountancy accountancy = (PersistentAccountancy) temp;

		// TODO "Rechnung Nr " deutsch?
		PersistentProductPaymentEntry ppe = new PersistentProductPaymentEntry(this.orderIdentifier,	this.userIdentifier, this.getTotalPrice(), "Rechnung Nr. " + this.orderIdentifier, this.paymentMethod);
		accountancy.add(ppe);
		orderStatus = OrderStatus.PAYED;
		return true;
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
	
	
	/*
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

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public final Order splitOrder() {
			if (this.orderCompletionStatus == OrderCompletionStatus.SPLITORDER) {
				if (this.entityManager.getTransaction().isActive()) {
					try {
						// Commit Fails -> Auto Rollback?
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
	*/

	@Override
	public int compareTo(PersistentOrder other) {
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
