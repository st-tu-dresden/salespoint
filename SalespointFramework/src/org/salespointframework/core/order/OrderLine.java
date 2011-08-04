package org.salespointframework.core.order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.joda.time.DateTime;
import org.salespointframework.core.inventory.Inventory;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.SerialNumber;
import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;
import org.salespointframework.util.SalespointIterable;

/**
 * 
 * @author Thomas Dedek
 * 
 */
@Entity
public class OrderLine {

	// TODO Unit Price über Inventar
	@EmbeddedId
	private OrderLineIdentifier identifier;

	@OneToMany(cascade = CascadeType.ALL)
	private List<ChargeLine> chargeLines;
	
	//TODO maybe ChargeLines for particular SerialNumbers
	//private Map<String,List<ChargeLine>> snChargeLines;

	// TODO Problems with multiple embedded Objects...
	@ElementCollection
	@AttributeOverride(name = "id", column = @Column(name = "SERIALNO"))
	private Set<SerialNumber> serialNumbers = new HashSet<SerialNumber>();
	@Transient
	private Inventory<?> inventory;

	private String description;
	private String comment;

	private Money unitPrice;
	@Temporal(TemporalType.TIMESTAMP)
	private Date expectedDeliveryDate;

	protected boolean mutableChargeLines;
	protected boolean mutableOrderLine;

	@Deprecated
	protected OrderLine() {
	}

	// PAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAUL
	public OrderLine(Inventory<?> inventory, SerialNumber serialNumber) {
		// check Inventar,
		this.inventory = Objects.requireNonNull(inventory, "inventory");
		Objects.requireNonNull(serialNumber, "serialNumber");
		this.serialNumbers.add(serialNumber);
	}

	public OrderLine(Inventory<?> inventory, Iterable<SerialNumber> serialNumber) {
		// check Inventar
		this.inventory = Objects.requireNonNull(inventory, "inventory");
		Objects.requireNonNull(serialNumber, "serialNumber");
		if (Iterables.isEmpty(serialNumber)) {
			// TODO bessere Exception
			throw new RuntimeException();
		}
		this.serialNumbers.addAll(Iterables.toList(serialNumber));
	}

	// PAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAUL
	public OrderLine(SerialNumber serialNumber, Inventory<?> inventory,
			String description, String comment, Money unitPrice,
			DateTime expectedDeliveryDate) {
		this.description = Objects.requireNonNull(description, "description");
		this.comment = Objects.requireNonNull(comment, "comment");
		this.unitPrice = Objects.requireNonNull(unitPrice, "unitPrice");
		this.identifier = new OrderLineIdentifier();
		this.expectedDeliveryDate = Objects.requireNonNull(
				expectedDeliveryDate, "expectedDeliveryDate").toDate();
		this.chargeLines = new ArrayList<ChargeLine>();
		this.mutableChargeLines = true;
		this.mutableOrderLine = true;
		Objects.requireNonNull(serialNumber, "serialNumber");
		this.serialNumbers.add(serialNumber);
		this.inventory = Objects.requireNonNull(inventory, "inventory");
	}

	public OrderLine(Collection<SerialNumber> serialNumbers,
			Inventory<?> inventory, String description, String comment,
			Money unitPrice, DateTime expectedDeliveryDate) {
		this.description = Objects.requireNonNull(description, "description");
		this.comment = Objects.requireNonNull(comment, "comment");
		this.unitPrice = Objects.requireNonNull(unitPrice, "unitPrice");
		this.identifier = new OrderLineIdentifier();
		this.expectedDeliveryDate = Objects.requireNonNull(
				expectedDeliveryDate, "expectedDeliveryDate").toDate();
		this.chargeLines = new ArrayList<ChargeLine>();
		this.mutableChargeLines = true;
		this.mutableOrderLine = true;

		Objects.requireNonNull(serialNumbers, "serialNumbers");
		this.serialNumbers = new HashSet<SerialNumber>();
		for (SerialNumber sn : serialNumbers) {
			this.serialNumbers.add(sn);
		}
		this.inventory = Objects.requireNonNull(inventory, "inventory");
	}

	/**
	 * @return the identifier
	 */
	public OrderLineIdentifier getIdentifier() {
		return identifier;
	}

	/**
	 * This Method returns an Iterable of ChargeLines from this OrderLine.
	 * 
	 * @return the Iterable with ChargeLines from this OrderLine
	 */
	public Iterable<ChargeLine> getChargeLines() {
		return SalespointIterable.from(this.chargeLines);
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return this.comment;
	}

	/**
	 * @return the numberOrdered
	 */
	public int getNumberOrdered() {
		return this.serialNumbers.size();
	}

	/**
	 * @return the unitPrice
	 */
	public Money getUnitPrice() {
		return unitPrice;
	}

	/**
	 * Calculates the total price of this OrderLine. The number of ordered
	 * objects and ChargeLines are included in the calculation.
	 * 
	 * @return the total price of this OrderLine
	 */
	public Money getOrderLinePrice() {
		
		Iterator<SerialNumber> itSN = this.serialNumbers.iterator();
		
		Money price = this.inventory.getProductInstance(itSN.next()).getPrice();
		
		while(itSN.hasNext()) {
			price = price.add_(this.inventory.getProductInstance(itSN.next()).getPrice());
		}
		
		for (ChargeLine cl : this.chargeLines) {
			price = price.add_(cl.getAmount());
		}

		return price;
	}

	/**
	 * @return the expectedDeliveryDate
	 */
	public DateTime getExpectedDeliveryDate() {
		return new DateTime(expectedDeliveryDate);
	}

	/**
	 * Adds the given SerialNumber to this OrderLine. If this
	 * OrderLine is provided in the context of an processing, cancelled or
	 * closed OrderEntry or the inventory doesn't contain this SerialNumber, the number cannot be added.
	 * 
	 * @param serialNumber
	 *            the SerialNumber that shall to be added.
	 *            
	 * @return true if this OrderLine changed as a result of the call.
	 */
	public boolean addSerialNumber(SerialNumber serialNumber) {

		if (!this.mutableChargeLines)
			return false;
		Objects.requireNonNull(serialNumber, "serialNumber");
		if(!inventory.contains(serialNumber)) 
			return false;
		return this.serialNumbers.add(serialNumber);
	}

	/**
	 * Adds the specified SerialNumbers to this OrderLine. If this
	 * OrderLine is provided in the context of an processing, cancelled or
	 * closed OrderEntry or the inventory doesn't contain all SerialNumbers of the given Iterable, no number will be added.
	 * 
	 * @param serialNumbers
	 *            the SerialNumbers that shall to be added.
	 *            
	 * @return true if this OrderLine changed as a result of the call.
	 */
	public boolean addAllSerialNumbers(Iterable<SerialNumber> serialNumbers) {

		if (!this.mutableChargeLines)
			return false;
		Objects.requireNonNull(serialNumbers, "serialNumbers");
		
		for(SerialNumber sn : serialNumbers) {
			if(!inventory.contains(sn)) 
				return false;
		}
		return this.serialNumbers.addAll(Iterables.toList(serialNumbers));
	}
	
	/**
	 * Removes the given SerialNumber from this OrderLine. If this
	 * OrderLine is provided in the context of an processing, cancelled or
	 * closed OrderEntry, the number cannot be removed.
	 * 
	 * @param serialNumber
	 *            the SerialNumber that shall to be removed.
	 *            
	 * @return true if this OrderLine changed as a result of the call.
	 */
	public boolean removeSerialNumber(SerialNumber serialNumber) {

		if (!this.mutableChargeLines)
			return false;
		Objects.requireNonNull(serialNumber, "serialNumber");
		if(!inventory.contains(serialNumber)) 
			return false;
		return this.serialNumbers.remove(serialNumber);
	}

	/**
	 * Removes the specified SerialNumbers from this OrderLine. If this
	 * OrderLine is provided in the context of an processing, cancelled or
	 * closed OrderEntry, no number will be removed.
	 * 
	 * @param serialNumbers
	 *            the SerialNumbers that shall to be removed.
	 *            
	 * @return true if this OrderLine changed as a result of the call. 
	 */
	public boolean removeAllSerialNumbers(Iterable<SerialNumber> serialNumbers) {

		if (!this.mutableChargeLines)
			return false;
		Objects.requireNonNull(serialNumbers, "serialNumbers");
		
		for(SerialNumber sn : serialNumbers) {
			if(!inventory.contains(sn)) 
				return false;
		}
		return this.serialNumbers.removeAll(Iterables.toList(serialNumbers));
	}
	

	/**
	 * Add a <code>ChargeLine</code> to this <code>OrderLine</code>. The
	 * ChargeLine cannot be added, if this OrderLine is provided in the context
	 * of an cancelled or closed OrderEntry.
	 * 
	 * @param chargeLine
	 *            The <code>ChargeLine</code> that shall be added.
	 */
	public boolean addChargeLine(ChargeLine chargeLine) {

		Objects.requireNonNull(chargeLine, "chargeLine");
		if (!this.mutableChargeLines)
			return false;

		return this.chargeLines.add(chargeLine);
	}

	/**
	 * Remove a <code>ChargeLine</code> from this <code>OrderLine</code>. The
	 * ChargeLine cannot be removed, if this OrderLine is provided in the
	 * context of an cancelled or closed OrderEntry.
	 * 
	 * @param id
	 *            The Identifier from the <code>ChargeLine</code> that shall be
	 *            removed.
	 */
	public boolean removeChargeLine(OrderLineIdentifier id) {

		Objects.requireNonNull(id, "id");
		if (!this.mutableChargeLines)
			return false;

		ChargeLine lineToRemove = null;
		boolean available = false;

		for (ChargeLine cl : this.chargeLines) {
			if (cl.getIdentifier().equals(id)) {
				lineToRemove = cl;
				available = true;
				break;
			}
		}

		if (available == false)
			return false;
		else
			return this.chargeLines.remove(lineToRemove);
	}

	public Inventory<?> getInventory() {
		return this.inventory;
	}

	public Iterable<SerialNumber> getSerialNumbers() {
		return SalespointIterable.from(this.serialNumbers);
	}

	@Override
	public boolean equals(Object other) {
		if (other == this)
			return true;
		if (other == null)
			return false;
		if (!(other instanceof OrderLine))
			return false;
		return this.equals((OrderLine) other);
	}

	public boolean equals(OrderLine other) {
		if (other == this)
			return true;
		if (other == null)
			return false;
		return this.getIdentifier().equals(other.getIdentifier());
	}

	@Override
	public int hashCode() {
		return this.getIdentifier().hashCode();
	}
}
