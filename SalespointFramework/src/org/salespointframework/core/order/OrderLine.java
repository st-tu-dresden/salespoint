package org.salespointframework.core.order;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.OneToMany;

import org.salespointframework.core.database.Database;
import org.salespointframework.core.inventory.Inventory;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.SerialNumber;
import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;

/**
 * 
 * @author Thomas Dedek
 * 
 */
@Entity
public class OrderLine {

	@EmbeddedId
	@AttributeOverride(name = "id", column = @Column(name = "ORDERLINE_ID"))
	private OrderLineIdentifier identifier;

	@OneToMany(cascade = CascadeType.ALL)
	private Set<ChargeLine> chargeLines;

	// Look, i FIXED it, hope u liek
	@ElementCollection
	private Set<SerialNumber> serialNumbers = new HashSet<SerialNumber>();

	//TODO reinitialize
/*	@Transient
	private Inventory<?> inventory;*/
	
	//for recovering the transient inventory
	private String inventoryKey;

	private String description;
	private String comment;

	//private Money unitPrice; -> removed, because of calculation over the inventory
	
	//@Temporal(TemporalType.TIMESTAMP)
	//private Date expectedDeliveryDate; -> not Used

	protected boolean mutableChargeLines;
	protected boolean mutableOrderLine;

	@Deprecated
	protected OrderLine() {
		

	}

	public OrderLine(Inventory<?> inventory, SerialNumber serialNumber) {

		this.inventoryKey = inventory.getClass().getCanonicalName();
		Objects.requireNonNull(serialNumber, "serialNumber");
		
		if(inventory.contains(serialNumber)) {
			this.serialNumbers.add(serialNumber);
		} else {
			// TODO bessere Exception
			throw new RuntimeException();
		}
		
		this.identifier = new OrderLineIdentifier();
		this.description = "";
		this.comment = "";
		//this.unitPrice = Objects.requireNonNull(unitPrice, "unitPrice");
		this.chargeLines = new HashSet<ChargeLine>();
		this.mutableChargeLines = true;
		this.mutableOrderLine = true;
	}

	public OrderLine(Inventory<?> inventory, Iterable<SerialNumber> serialNumbers) {

		this.inventoryKey = inventory.getClass().getCanonicalName();
		Objects.requireNonNull(serialNumbers, "serialNumber");
		if (Iterables.isEmpty(serialNumbers)) {
			// TODO bessere Exception
			throw new RuntimeException();
		}
		
		for(SerialNumber sn : serialNumbers) {
			if(!inventory.contains(sn)) {
				// TODO bessere Exception
				throw new RuntimeException();
			}
		}
		
		this.serialNumbers.addAll(Iterables.toList(serialNumbers));
		
		this.identifier = new OrderLineIdentifier();
		this.description = "";
		this.comment = "";
		//this.unitPrice = Objects.requireNonNull(unitPrice, "unitPrice");
		this.chargeLines = new HashSet<ChargeLine>();
		this.mutableChargeLines = true;
		this.mutableOrderLine = true;
	}

	public OrderLine(Inventory<?> inventory, SerialNumber serialNumber,
			String description, String comment) {

		this.inventoryKey = inventory.getClass().getCanonicalName();
		Objects.requireNonNull(serialNumber, "serialNumber");
		
		if(inventory.contains(serialNumber)) {
			this.serialNumbers.add(serialNumber);
		} else {
			// TODO bessere Exception
			throw new RuntimeException();
		}
		
		this.identifier = new OrderLineIdentifier();
		this.description = Objects.requireNonNull(description, "description");
		this.comment = Objects.requireNonNull(comment, "comment");
		//this.unitPrice = Objects.requireNonNull(unitPrice, "unitPrice");
		this.chargeLines = new HashSet<ChargeLine>();
		this.mutableChargeLines = true;
		this.mutableOrderLine = true;
	}

	public OrderLine(Inventory<?> inventory, Iterable<SerialNumber> serialNumbers, 
			String description, String comment) {

		this.inventoryKey = inventory.getClass().getCanonicalName();
		Objects.requireNonNull(serialNumbers, "serialNumber");
		if (Iterables.isEmpty(serialNumbers)) {
			// TODO bessere Exception
			throw new RuntimeException();
		}
		
		for(SerialNumber sn : serialNumbers) {
			if(!inventory.contains(sn)) {
				// TODO bessere Exception
				throw new RuntimeException();
			}
		}
		
		this.serialNumbers.addAll(Iterables.toList(serialNumbers));
		
		this.identifier = new OrderLineIdentifier();
		this.description = Objects.requireNonNull(description, "description");
		this.comment = Objects.requireNonNull(comment, "comment");
		//this.unitPrice = Objects.requireNonNull(unitPrice, "unitPrice");
		this.chargeLines = new HashSet<ChargeLine>();
		this.mutableChargeLines = true;
		this.mutableOrderLine = true;
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
		return Iterables.from(this.chargeLines);
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
	 *//*
	public Money getUnitPrice() {
		return unitPrice;
	}*/

	/**
	 * Calculates the total price of this OrderLine. The number of ordered
	 * objects and ChargeLines are included in the calculation.
	 * 
	 * @return the total price of this OrderLine
	 */
	public Money getOrderLinePrice() {
		
		Inventory<?> inventory = this.getInventory();
		
		Iterator<SerialNumber> itSN = this.serialNumbers.iterator();
		Money price = new Money(0);

		if (itSN.hasNext()) {
			price = inventory.getProductInstance(itSN.next()).getPrice();

			while (itSN.hasNext()) {
				price = price.add_(inventory.getProductInstance(itSN.next())
						.getPrice());
			}

			for (ChargeLine cl : this.chargeLines) {
				price = price.add_(cl.getAmount());
			}
		}

		return price;
			
	}

	/**
	 * @return the expectedDeliveryDate
	 */
	/*public DateTime getExpectedDeliveryDate() {
		return new DateTime(expectedDeliveryDate);
	}*/

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

		Inventory<?> inventory = this.getInventory();
		
		if (!this.mutableOrderLine)
			return false;
		Objects.requireNonNull(serialNumber, "serialNumber");
		if (!inventory.contains(serialNumber))
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
	 	
		Inventory<?> inventory = this.getInventory();
		
		if (!this.mutableOrderLine)
			return false;
		Objects.requireNonNull(serialNumbers, "serialNumbers");
			
		for(SerialNumber sn : serialNumbers) {
			if(!inventory.contains(sn)) 
				return false;
		}
		return this.serialNumbers.addAll(Iterables.toList(serialNumbers));
		
	}
	
	public boolean mergeOrderLine(OrderLine orderLine) {
		
		boolean containsAllSerialNumbers = true;
		boolean containsAllChargeLines = true;
		
		for(SerialNumber sn : orderLine.getSerialNumbers()) {
			if(!this.serialNumbers.contains(sn))
				containsAllSerialNumbers = false;
		}
		
		for(ChargeLine cl : orderLine.getChargeLines()) {
			if(!this.chargeLines.contains(cl))
				containsAllChargeLines = false;
		}
		
		if (!this.mutableOrderLine && !containsAllSerialNumbers)
			return false;
		if (!this.mutableChargeLines && !containsAllChargeLines)
			return false;
		if(containsAllSerialNumbers && containsAllChargeLines)
			return false;
		Objects.requireNonNull(orderLine, "orderLine");
		
		for(SerialNumber sn : orderLine.getSerialNumbers()) {
			this.addSerialNumber(sn);
		}
		
		for(ChargeLine cl : orderLine.getChargeLines()) {
			this.addChargeLine(cl);
		}
		return true;
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
		
		Inventory<?> inventory = this.getInventory();
		
		if (!this.mutableOrderLine)
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

		Inventory<?> inventory = this.getInventory();
		
		if (!this.mutableOrderLine)
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
		
	 	try {
	 		
	 		//recover transient inventory
        	EntityManager em = Database.INSTANCE.getEntityManagerFactory().createEntityManager();
	 		
	 		Class<?> inventoryClass = Class.forName(this.inventoryKey);
	 		Inventory<?> inventory = (Inventory<?>) inventoryClass.getConstructor(EntityManager.class).newInstance(em);
	 		
	 		return inventory;
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public Iterable<SerialNumber> getSerialNumbers() {
		return Iterables.from(this.serialNumbers);
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
