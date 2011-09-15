package org.salespointframework.core.order;

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
import javax.persistence.RollbackException;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.DateTime;
import org.salespointframework.core.accountancy.AccountancyEntryIdentifier;
import org.salespointframework.core.accountancy.PersistentAccountancy;
import org.salespointframework.core.accountancy.ProductPaymentEntry;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.inventory.Inventory;
import org.salespointframework.core.inventory.PersistentInventory;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.order.OrderCompletionResult.OrderCompletionStatus;
import org.salespointframework.core.product.PersistentProduct;
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
public class PersistentOrder implements Order<PersistentOrderLine, PersistentChargeLine>
{
	@EmbeddedId
	private OrderIdentifier orderIdentifier;

	@Embedded
	@AttributeOverride(name = "id", column = @Column(name = "OWNER_ID"))
	private UserIdentifier userIdentifier;

	// TODO zyklisch gut? überhaupt notwendig?
	@SuppressWarnings("unused")
	@Embedded
	@AttributeOverride(name = "id", column = @Column(name = "ACCOUNTANCYENTRY_ID"))
	private AccountancyEntryIdentifier accountancyEntryIdentifier;

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

	/**
	 * Parameterless constructor required for JPA. Do not use.
	 */
	@Deprecated
	public PersistentOrder()
	{
	}

	public PersistentOrder(UserIdentifier userIdentifier)
	{
		this(userIdentifier, "", "");
	}

	public PersistentOrder(UserIdentifier userIdentifier, String salesChannel)
	{
		this(userIdentifier, salesChannel, "");
	}

	public PersistentOrder(UserIdentifier userIdentifier, String salesChannel, String termsAndConditions)
	{
		this.salesChannel = Objects.requireNonNull(salesChannel, "salesChannel");
		this.termsAndConditions = Objects.requireNonNull(termsAndConditions, "termsAndConditions");
		this.userIdentifier = Objects.requireNonNull(userIdentifier, "userIdentifier");

		orderIdentifier = new OrderIdentifier();
		dateCreated = Shop.INSTANCE.getTime().getDateTime().toDate();
		orderStatus = OrderStatus.OPEN;
		this.log.add(new OrderLogEntry("OrderEntry initialized succesfully", ""));
	}

	@Override
	public final boolean addOrderLine(PersistentOrderLine orderLine)
	{
		if (orderStatus != OrderStatus.OPEN)
		{
			return false;
		}
		return orderLines.add(orderLine);
	}

	@Override
	public final boolean removeOrderLine(OrderLineIdentifier orderLineIdentifier)
	{
		if (orderStatus != OrderStatus.OPEN)
		{
			return false;
		}
		PersistentOrderLine temp = null;
		for (PersistentOrderLine pol : orderLines)
		{
			if (pol.getOrderLineIdentifier().equals(orderLineIdentifier))
			{
				temp = pol;
				break;
			}
		}
		return orderLines.remove(temp);
	}

	@Override
	public final boolean addChargeLine(PersistentChargeLine chargeLine)
	{
		if (orderStatus != OrderStatus.OPEN)
		{
			return false;
		}
		return chargeLines.add(chargeLine);
	}

	@Override
	public final boolean removeChargeLine(ChargeLineIdentifier chargeLineIdentifier)
	{
		if (orderStatus != OrderStatus.OPEN)
		{
			return false;
		}
		PersistentChargeLine temp = null;
		for (PersistentChargeLine pcl : chargeLines)
		{
			if (pcl.getChargeLineIdentifier().equals(chargeLineIdentifier))
			{
				temp = pcl;
				break;
			}
		}
		return chargeLines.remove(temp);
	}

	@Override
	public final String getTermsAndConditions()
	{
		return termsAndConditions;
	}

	@Override
	public final Iterable<PersistentOrderLine> getOrderLines()
	{
		return Iterables.from(orderLines);
	}

	@Override
	public final Iterable<PersistentChargeLine> getChargeLines()
	{
		return Iterables.from(chargeLines);
	}

	@Override
	public final OrderStatus getOrderStatus()
	{
		return orderStatus;
	}

	@Override
	public final boolean cancelOrder()
	{
		if (orderStatus == OrderStatus.COMPLETED)
		{
			return false;
		}
		orderStatus = OrderStatus.CANCELLED;
		return true;
	}

	@Override
	public final Iterable<OrderLogEntry> getLogEntries()
	{
		return Iterables.from(log);
	}

	@Override
	public final Money getTotalPrice()
	{
		return this.getOrderedLinesPrice().add_(this.getChargeLinesPrice());
	}

	@Override
	public final Money getOrderedLinesPrice()
	{
		Money price = Money.ZERO;
		for (OrderLine orderLine : orderLines)
		{
			price = price.add_(orderLine.getPrice());
		}
		return price;
	}

	@Override
	public final Money getChargeLinesPrice()
	{
		Money price = Money.ZERO;
		for (ChargeLine chargeLine : chargeLines)
		{
			price = price.add_(chargeLine.getPrice());
		}
		return price;

	}

	@Override
	public boolean equals(Object other)
	{
		if (other == null)
		{
			return false;
		}
		if (other == this)
		{
			return true;
		}
		if (!(other instanceof PersistentOrder))
		{
			return false;
		}
		return this.equals((PersistentOrder) other);
	}

	public final boolean equals(PersistentOrder other)
	{
		if (other == this)
		{
			return true;
		}
		if (other == null)
		{
			return false;
		}
		return this.orderIdentifier.equals(other.orderIdentifier);
	}

	@Override
	public final int hashCode()
	{
		return this.orderIdentifier.hashCode();
	}

	// TODO
	@Override
	public String toString()
	{
		return "TODO";
	}

	@Override
	public final DateTime getCreationDate()
	{
		return new DateTime(dateCreated);
	}

	// TODO
	@Override
	public final OrderCompletionResult completeOrder()
	{
		// TODO mehr CompletionStates? oder letzten Param (Exception)
		// überdenken, String cause?
		if (orderStatus == OrderStatus.CANCELLED)
		{
			return new InternalOrderCompletionResult(OrderCompletionStatus.FAILED, null, null, null);
		}
		if (orderStatus == OrderStatus.COMPLETED)
		{
			return new InternalOrderCompletionResult(OrderCompletionStatus.FAILED, null, null, null);
		}
		if (orderStatus == OrderStatus.PAYED)
		{
			return new InternalOrderCompletionResult(OrderCompletionStatus.FAILED, null, null, null);
		}

		Inventory<?> tempInventory = Shop.INSTANCE.getInventory();
		if (!(tempInventory instanceof PersistentInventory))
		{
			throw new RuntimeException("Sorry, PersistentInventory only :("); // TODO
																				// Exception
																				// Name
		}

		EntityManager em = Database.INSTANCE.getEntityManagerFactory().createEntityManager();

		PersistentInventory inventory = ((PersistentInventory) tempInventory).createNew(em);
		Set<PersistentOrderLine> remainingOrderLines = new HashSet<PersistentOrderLine>(this.orderLines);

		em.getTransaction().begin();

		for (PersistentOrderLine orderLine : orderLines)
		{
			remainingOrderLines.remove(orderLine);
			Iterable<PersistentProduct> tempProducts = inventory.find(PersistentProduct.class, orderLine.getProductIdentifier(), orderLine.getProductFeatures());

			List<PersistentProduct> products = Iterables.toList(tempProducts);

			int numberOrdered = orderLine.getNumberOrdered();
			int removed = 0;

			for (PersistentProduct product : products)
			{
				boolean result = inventory.remove(product.getSerialNumber());

				if (!result)
				{
					PersistentOrder order = new PersistentOrder(this.userIdentifier, this.salesChannel, this.termsAndConditions);
					PersistentOrderLine pol = new PersistentOrderLine(orderLine.getProductIdentifier(), orderLine.getProductFeatures(), numberOrdered - removed);
					order.orderLines.add(pol);
					order.orderLines.addAll(remainingOrderLines);
					order.chargeLines.addAll(this.chargeLines);
					order.orderStatus = OrderStatus.PAYED;

					return new InternalOrderCompletionResult(OrderCompletionStatus.SPLITORDER, order, em, null);

				} else
				{
					removed++;
				}

				if (removed >= numberOrdered)
				{
					break;
				}
			}
		}

		try
		{
			em.getTransaction().commit();
		} catch (RollbackException e)
		{ // "RollbackException - if the commit fails"
			return new InternalOrderCompletionResult(OrderCompletionStatus.FAILED, null, em, e.getCause());
		}

		return new InternalOrderCompletionResult(OrderCompletionStatus.SUCCESSFUL, null, em, null);
	}

	// convenience
	public boolean isPayed()
	{
		return orderStatus == OrderStatus.PAYED;
	}

	@Override
	public boolean payOrder(/* TODO PaymentMethod paymentMethod */)
	{
		if (!(orderStatus == OrderStatus.PAYED))
		{
			return false;
		};
		ProductPaymentEntry ppe = new ProductPaymentEntry(this.orderIdentifier, this.userIdentifier, this.getTotalPrice());
		PersistentAccountancy pA = (PersistentAccountancy) Shop.INSTANCE.getAccountancy();
		pA.add(ppe);
		accountancyEntryIdentifier = ppe.getAccountancyEntryIdentifier();
		orderStatus = OrderStatus.PAYED;
		return true;
	}

	// gotta love inner classes
	private final class InternalOrderCompletionResult implements OrderCompletionResult
	{

		private final OrderCompletionStatus orderCompletionStatus;
		private final PersistentOrder order;
		private final EntityManager entityManager;
		private Throwable exception;

		public InternalOrderCompletionResult(OrderCompletionStatus orderCompletionStatus, PersistentOrder order, EntityManager entityManager, Throwable exception)
		{
			this.orderCompletionStatus = orderCompletionStatus;
			this.order = order;
			this.entityManager = entityManager;
			this.exception = exception;
		}

		@SuppressWarnings({"unchecked", "rawtypes"})
		// TODO
		@Override
		public final Order splitOrder()
		{
			if (this.orderCompletionStatus == OrderCompletionStatus.SPLITORDER)
			{
				if (this.entityManager.getTransaction().isActive())
				{
					try
					{
						this.entityManager.getTransaction().commit(); // TODO
																		// Commit
																		// Fails
																		// ->
																		// Auto
																		// Rollback?
					} catch (RollbackException e)
					{
						this.exception = e.getCause();
						return null;
					}
				}
				return order;
			}
			return null;
		}

		@Override
		public final boolean rollBack()
		{
			if (this.orderCompletionStatus == OrderCompletionStatus.SPLITORDER)
			{
				if (this.entityManager.getTransaction().isActive())
				{
					this.entityManager.getTransaction().rollback();
					return true;
				}
				{
					return false;
				}
			} else
			{
				return false;
			}
		}

		@Override
		public final OrderCompletionStatus getStatus()
		{
			return this.orderCompletionStatus;
		}

		@Override
		public Throwable getException()
		{
			return this.exception;
		}
	}
}
