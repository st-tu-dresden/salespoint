package org.salespointframework.core.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.DateTime;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.order.actions.OrderAction;
import org.salespointframework.util.Objects;
import org.salespointframework.util.SalespointIterable;

@Entity
public class OrderEntry {
	
	@EmbeddedId
	private OrderIdentifier orderIdentifier;
	
	// Do NOT fucking touch!
	@Column(name = "TimeStamp", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@SuppressWarnings("unused")
	private Date timeStamp;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated;
	private String salesChannel;
	private String termsAndConditions;
	
	
	private List<OrderAction> orderActions;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<OrderLine> orderLines;
	@OneToMany(cascade = CascadeType.ALL)
	private List<ChargeLine> chargeLines;
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	
	public OrderEntry(String salesChannel, String termsAndConditions) {
		orderIdentifier = new OrderIdentifier();
		dateCreated = new Date();
		this.salesChannel = Objects
				.requireNonNull(salesChannel, "salesChannel");
		this.termsAndConditions = Objects.requireNonNull(termsAndConditions,
				"termsAndConditions");
		orderActions = new ArrayList<OrderAction>();
		orderLines = new ArrayList<OrderLine>();
		status = OrderStatus.INITIALIZED;
	}

	public OrderEntry(String salesChannel) {
		this(Objects.requireNonNull(salesChannel, "salesChannel"), "");
	}
		
	public OrderEntry() {
		this("", "");
	}

	public String toString() {
		return orderIdentifier.toString();
	}

	/**
	 * @return the orderIdentifier
	 */
	public OrderIdentifier getOrderIdentifier() {
		return orderIdentifier;
	}

	/**
	 * @return the dateCreated
	 */
	public DateTime getDateCreated() {
		return new DateTime(dateCreated);
	}

	/**
	 * @return the salesChannel
	 */
	public String getSalesChannel() {
		return salesChannel;
	}

	/**
	 * @return the termsAndConditions
	 */
	public String getTermsAndConditions() {
		return termsAndConditions;
	}

	/**
	 * @return the orderAction
	 */
	public Iterable<OrderAction> getOrderAction() {
		return SalespointIterable.from(this.orderActions);
	}
	
	/**
	 * @return the orderLines
	 */
	public Iterable<OrderLine> getOrderLines() {
		return SalespointIterable.from(this.orderLines);
	}
	
	/**
	 * @return the chargeLines
	 */
	public Iterable<ChargeLine> getChargeLines() {
		return SalespointIterable.from(this.chargeLines);
	}
	
	/**
	 * Calculates the total price of this OrderEntry. The number of ordered objects and ChargeLines are included in the calculation.
	 * 
	 * @return the total price of this OrderEntry
	 */
	public Money getTotalPrice() {
		
		Money price;	
		boolean hasOrderLines = !this.orderLines.isEmpty();
		
		if(this.chargeLines.isEmpty() && !hasOrderLines) return new Money(0);
		if(hasOrderLines) price = this.orderLines.get(0).getOrderLinePrice();
		else price = this.chargeLines.get(0).getAmount();
		
		if(hasOrderLines) {
			
			for(int i=1; i<this.orderLines.size(); i++) {
				price = (Money) price.add(this.orderLines.get(i).getOrderLinePrice());
			}
			
			for(int i=0; i<this.chargeLines.size(); i++) {
				price = (Money) price.add(this.chargeLines.get(i).getAmount());
			}
		} else {
			
			for(int i=1; i<this.chargeLines.size(); i++) {
				price = (Money) price.add(this.chargeLines.get(i).getAmount());
			}
		}

		return price;
	}
	
	/**
	 * Add a <code>ChargeLine</code> to this
	 * <code>OrderEntry</code>.
	 * 
	 * @param chargeLine The <code>ChargeLine</code> that shall be added.
	 */
	public boolean addChargeLine(ChargeLine chargeLine) {
		
		Objects.requireNonNull(chargeLine, "chargeLine");
		return this.chargeLines.add(chargeLine);
	}
	
	/**
	 * Remove a <code>ChargeLine</code> from this
	 * <code>OrderEntry</code>.
	 * 
	 * @param id The Identifier from the <code>ChargeLine</code> that shall be removed.
	 */
	public boolean removeChargeLine(OrderLineIdentifier id) {
		
		Objects.requireNonNull(id, "id");
		
		ChargeLine lineToRemove = null;
		boolean available = false;
		
		for(ChargeLine cl : this.chargeLines) {
			if(cl.getIdentifier().equals(id)) {
				lineToRemove = cl;
				available = true;
				break;
			}
		}
		
		if(available == false) return false;
		else return this.chargeLines.remove(lineToRemove);
	}
	
	/**
	 * Add an <code>OrderLine</code> to this
	 * <code>OrderEntry</code>.
	 * 
	 * @param orderLine The <code>OrderLine</code> that shall be added.
	 */
	public boolean addOrderLine(OrderLine orderLine) {
		
		Objects.requireNonNull(orderLine, "orderLine");
		return this.orderLines.add(orderLine);
	}
	
	/**
	 * Remove an <code>OrderLine</code> from this
	 * <code>OrderEntry</code>.
	 * 
	 * @param id The Identifier from the <code>OrderLine</code> that shall be removed.
	 */
	public boolean removeOrderLine(OrderLineIdentifier id) {
		
		Objects.requireNonNull(id, "id");
		
		OrderLine lineToRemove = null;
		boolean available = false;
		
		for(OrderLine ol : this.orderLines) {
			if(ol.getIdentifier().equals(id)) {
				lineToRemove = ol;
				available = true;
				break;
			}
		}
		
		if(available == false) return false;
		else return this.orderLines.remove(lineToRemove);
	}

}
