package org.salespointframework.core.order;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import org.salespointframework.core.database.Database;
import org.salespointframework.core.inventory.Inventory;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.SerialNumber;
import org.salespointframework.core.shop.Shop;
import org.salespointframework.core.users.UserIdentifier;
import org.salespointframework.util.Objects;
import org.salespointframework.util.SalespointIterable;

@Entity
public class OrderEntry {
	
	@EmbeddedId
	@AttributeOverride(name = "id", column = @Column(name = "ORDERENTRY_ID"))
	private OrderIdentifier orderIdentifier;
	
	//for internal association with users
	@Embedded
	@AttributeOverride(name = "id", column = @Column(name = "OWNER_ID"))
	protected UserIdentifier uID;
	
	// Do NOT fucking touch!
	@Column(name = "TimeStamp", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@SuppressWarnings("unused")
	private Date timeStamp;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated;
	private String salesChannel;
	private String termsAndConditions;
	
	
	@OneToMany(cascade = CascadeType.ALL)
	private Set<ChargeLine> chargeLines = new HashSet<ChargeLine>();
	@OneToMany(cascade = CascadeType.ALL)
	private Map<String, OrderLine> orderlines = new HashMap<String, OrderLine>();
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	@ElementCollection
	private List<OrderLogEntry> log = new ArrayList<OrderLogEntry>();
	
	@Deprecated
	public OrderEntry() {
	}
	
	public OrderEntry(UserIdentifier userIdentifier, String salesChannel) {
		this(Objects.requireNonNull(userIdentifier, "userIdentifier"), 
				Objects.requireNonNull(salesChannel, "salesChannel"), "");
	}
		
	public OrderEntry(UserIdentifier userIdentifier, String salesChannel, String termsAndConditions) {
		orderIdentifier = new OrderIdentifier();
		dateCreated = Shop.INSTANCE.getTime().getDateTime().toDate();
		this.salesChannel = Objects
				.requireNonNull(salesChannel, "salesChannel");
		this.termsAndConditions = Objects.requireNonNull(termsAndConditions,
				"termsAndConditions");
		this.uID = Objects.requireNonNull(userIdentifier,
				"userIdentifier");
		status = OrderStatus.INITIALIZED;
	}


	//TODO make PaymentEntry in Accountancy
	public boolean completeOrder() {
		EntityManager em = Database.INSTANCE.getEntityManagerFactory().createEntityManager();
		
		em.getTransaction().begin();
		
		for(Map.Entry<String, OrderLine> entry : orderlines.entrySet()) {
			String key = entry.getKey();
			OrderLine orderLine = entry.getValue();
			
			
			try {
				Class<?> inventoryClass = Class.forName(key);
				
			 	Inventory<?> inventory = (Inventory<?>) inventoryClass.getConstructor(EntityManager.class).newInstance(em);
				
			 	for(SerialNumber serialNumber : orderLine.getSerialNumbers()) {
			 		inventory.removeProductInstance(serialNumber);
			 	}
				
			 	
			// TODO plätttten
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
			}
		}
		em.getTransaction().commit();
		
		return false;
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
	 * @return the orderStatus
	 */
	public OrderStatus getOrderStatus() {
		return this.status;
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
	 * @return the orderLines
	 */
	public Iterable<OrderLine> getOrderLines() {
		return SalespointIterable.from(this.orderlines.values());
	}
	
	/**
	 * @return an Iterable with all chargeLines from this OrderEntry
	 */
	public Iterable<ChargeLine> getChargeLines() {
		return SalespointIterable.from(this.chargeLines);
	}
	
	/**
	 * @return an Iterable with all LogEntrys from this OrderEntry
	 */
	public Iterable<OrderLogEntry> getLogEntries() {
		return SalespointIterable.from(this.log);
	}
	
	/**
	 * Calculates the total price of this OrderEntry. The number of ordered objects and ChargeLines are included in the calculation.
	 * 
	 * @return the total price of this OrderEntry
	 */
	public Money getTotalPrice() {
		
		Money price;	
		
		if(!this.orderlines.isEmpty()) {
			price = this.getOrderedObjectsPrice();
			price.add_(this.getChargedPrice());
		} else {
			price = this.getChargedPrice();
		}
		return price;
	}
	
	/**
	 * Calculates the price of all ordered objects from this OrderEntry without including ChargeLines.
	 * 
	 * @return the cumulative price of ordered objects from this OrderEntry
	 */
	public Money getOrderedObjectsPrice() {
		
		Money price = new Money(0);
		boolean firstElement = true;
		
		for(OrderLine ol : this.orderlines.values()) {
			if(firstElement) {
				price = ol.getOrderLinePrice();
				firstElement = false;
			} else {
				price = price.add_(ol.getOrderLinePrice());
			}
		}
		
		return price;
	}
	
	/**
	 * Calculates the price of all ChargeLines from this OrderEntry.
	 * 
	 * @return the charged price of this OrderEntry
	 */
	public Money getChargedPrice() {
		
		Money price = new Money(0);
		boolean firstElement = true;
		
		for(ChargeLine cl : this.chargeLines) {
			if(firstElement) {
				price = cl.getAmount();
				firstElement = false;
			} else {
				price = price.add_(cl.getAmount());
			}
		}
		
		return price;
	}
	
	/**
	 * Add a <code>ChargeLine</code> to this
	 * <code>OrderEntry</code> with standard log entry. 
	 * The ChargeLine cannot be added, if this OrderEntry is cancelled or closed.
	 * 
	 * @param chargeLine The <code>ChargeLine</code> that shall be added.
	 */
	public boolean addChargeLine(ChargeLine chargeLine) {
		
		Objects.requireNonNull(chargeLine, "chargeLine");
		if(this.status.equals(OrderStatus.CANCELLED) || this.status.equals(OrderStatus.CLOSED)) return false;
		
		if(this.chargeLines.add(chargeLine)) {
			this.log.add(new OrderLogEntry("ChargeLine: "+chargeLine.toString()+" added successfully", ""));
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Add a <code>ChargeLine</code> to this
	 * <code>OrderEntry</code> with defined log entry. 
	 * The ChargeLine cannot be added, if this OrderEntry is cancelled or closed.
	 * 
	 * @param chargeLine The <code>ChargeLine</code> that shall be added.
	 */
	public boolean addChargeLine(ChargeLine chargeLine, String logDescription) {
		
		Objects.requireNonNull(chargeLine, "chargeLine");
		Objects.requireNonNull(logDescription, "logDescription");
		if(this.status.equals(OrderStatus.CANCELLED) || this.status.equals(OrderStatus.CLOSED)) return false;
		
		if(this.chargeLines.add(chargeLine)) {
			this.log.add(new OrderLogEntry("ChargeLine: "+chargeLine.toString()+" added successfully", logDescription));
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Remove a <code>ChargeLine</code> from this
	 * <code>OrderEntry</code> with standard log entry. The ChargeLine cannot be removed, 
	 * if this OrderEntry is cancelled or closed.
	 * 
	 * @param id The Identifier from the <code>ChargeLine</code> that shall be removed.
	 */
	public boolean removeChargeLine(OrderLineIdentifier id) {
		
		Objects.requireNonNull(id, "id");
		if(this.status.equals(OrderStatus.CANCELLED) || this.status.equals(OrderStatus.CLOSED)) return false;
		
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
		else if(this.chargeLines.remove(lineToRemove)) {
				this.log.add(new OrderLogEntry("ChargeLine: "+lineToRemove.toString()+" removed successfully", ""));
				return true;
			} else {
				return false;
		}
	}
	
	/**
	 * Remove a <code>ChargeLine</code> from this
	 * <code>OrderEntry</code> with defined log entry. The ChargeLine cannot be removed, 
	 * if this OrderEntry is cancelled or closed.
	 * 
	 * @param id The Identifier from the <code>ChargeLine</code> that shall be removed.
	 */
	public boolean removeChargeLine(OrderLineIdentifier id, String logDescription) {
		
		Objects.requireNonNull(id, "id");
		Objects.requireNonNull(logDescription, "logDescription");
		if(this.status.equals(OrderStatus.CANCELLED) || this.status.equals(OrderStatus.CLOSED)) return false;
		
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
		else if(this.chargeLines.remove(lineToRemove)) {
				this.log.add(new OrderLogEntry("ChargeLine: "+lineToRemove.toString()+" removed successfully", logDescription));
				return true;
			} else {
				return false;
		}
	}
	
	/**
	 * Adds an <code>OrderLine</code> to this
	 * <code>OrderEntry</code> with standard log entry. If the OrderLine already exists in this OrderEntry,
	 * the OrderLine will be merged with the existing OrderLine. The OrderLine cannot be added, 
	 * if this OrderEntry is processing, cancelled or closed.
	 * 
	 * @param orderLine The <code>OrderLine</code> that shall be added.
	 */
	public OrderLine addOrderLine(OrderLine orderLine) {
		Objects.requireNonNull(orderLine, "orderLine");
		if(this.status.equals(OrderStatus.CANCELLED) || this.status.equals(OrderStatus.CLOSED) || this.status.equals(OrderStatus.PROCESSING)) {
			// TODO andere bessere Exception
			throw new RuntimeException();
		}
		
		String key = orderLine.getInventory().getClass().getCanonicalName();
		OrderLine o = orderLine;
		
		if(orderlines.containsKey(key)) {
			 orderlines.get(key).mergeOrderLine(o);
			 this.log.add(new OrderLogEntry("OrderLine: "+orderLine.toString()+" merged succesfully", ""));
		} else {
			orderlines.put(key, o);
			this.log.add(new OrderLogEntry("OrderLine: "+orderLine.toString()+" added succesfully", ""));
		}
		
		return orderlines.get(key);
	}
	
	/**
	 * Adds an <code>OrderLine</code> to this
	 * <code>OrderEntry</code> with defined log entry. If the OrderLine already exists in this OrderEntry,
	 * the OrderLine will be merged with the existing OrderLine. The OrderLine cannot be added, 
	 * if this OrderEntry is processing, cancelled or closed.
	 * 
	 * @param orderLine The <code>OrderLine</code> that shall be added.
	 */
	public OrderLine addOrderLine(OrderLine orderLine, String logDescription) {
		Objects.requireNonNull(orderLine, "orderLine");
		if(this.status.equals(OrderStatus.CANCELLED) || this.status.equals(OrderStatus.CLOSED) || this.status.equals(OrderStatus.PROCESSING)) {
			// TODO andere bessere Exception
			throw new RuntimeException();
		}
		
		String key = orderLine.getInventory().getClass().getCanonicalName();
		OrderLine o = orderLine;
		
		if(orderlines.containsKey(key)) {
			 orderlines.get(key).mergeOrderLine(o);
			 this.log.add(new OrderLogEntry("OrderLine: "+orderLine.toString()+" merged succesfully", logDescription));
		} else {
			orderlines.put(key, o);
			this.log.add(new OrderLogEntry("OrderLine: "+orderLine.toString()+" added succesfully", logDescription));
		}
		
		return orderlines.get(key);
	}
	
	/**
	 * Adds an <code>OrderLine</code> to this
	 * <code>OrderEntry</code> with standard log entry. The OrderLine cannot be added, 
	 * if this OrderEntry is processing, cancelled or closed.
	 * 
	 * @param orderLine The <code>OrderLine</code> that shall be added.
	 */
	public OrderLine addOrderLine(Inventory<?> inventory, SerialNumber serialNumber) {
		
		Objects.requireNonNull(inventory, "inventory");
		Objects.requireNonNull(serialNumber, "serialNumber");
		if(this.status.equals(OrderStatus.CANCELLED) || this.status.equals(OrderStatus.CLOSED) || this.status.equals(OrderStatus.PROCESSING)) {
			// TODO andere bessere Exception
			throw new RuntimeException();
		}
		
		String key = inventory.getClass().getCanonicalName();
		OrderLine o;
		
		if(orderlines.containsKey(key)) {
			orderlines.get(key).addSerialNumber(serialNumber);
			this.log.add(new OrderLogEntry("OrderLine: "+orderlines.get(key).toString()+" merged succesfully", ""));
			o = orderlines.get(key);
		} else {
			o = new OrderLine(inventory, serialNumber);
			orderlines.put(key,o);
			this.log.add(new OrderLogEntry("OrderLine: "+o.toString()+" added succesfully", ""));
		}
		
		return o;
	}
	
	/**
	 * Adds an <code>OrderLine</code> to this
	 * <code>OrderEntry</code> with defined log entry. The OrderLine cannot be added, 
	 * if this OrderEntry is processing, cancelled or closed.
	 * 
	 * @param orderLine The <code>OrderLine</code> that shall be added.
	 */
	public OrderLine addOrderLine(Inventory<?> inventory, SerialNumber serialNumber, String logDescription) {
		
		Objects.requireNonNull(inventory, "inventory");
		Objects.requireNonNull(serialNumber, "serialNumber");
		if(this.status.equals(OrderStatus.CANCELLED) || this.status.equals(OrderStatus.CLOSED) || this.status.equals(OrderStatus.PROCESSING)) {
			// TODO andere bessere Exception
			throw new RuntimeException();
		}
		
		String key = inventory.getClass().getCanonicalName();
		OrderLine o;
		
		if(orderlines.containsKey(key)) {
			orderlines.get(key).addSerialNumber(serialNumber);
			this.log.add(new OrderLogEntry("OrderLine: "+orderlines.get(key).toString()+" merged succesfully", logDescription));
			o = orderlines.get(key);
		} else {
			o = new OrderLine(inventory, serialNumber);
			orderlines.put(key,o);
			this.log.add(new OrderLogEntry("OrderLine: "+orderlines.get(key).toString()+" added succesfully", logDescription));
		}
		
		return o;
	}
	
	/**
	 * Adds an <code>OrderLine</code> to this
	 * <code>OrderEntry</code> with standard log entry. The OrderLine cannot be added, 
	 * if this OrderEntry is processing, cancelled or closed.
	 * 
	 * @param orderLine The <code>OrderLine</code> that shall be added.
	 */
	public OrderLine addOrderLine(Inventory<?> inventory, Iterable<SerialNumber> serialNumbers) {
		
		Objects.requireNonNull(inventory, "inventory");
		Objects.requireNonNull(serialNumbers, "serialNumbers");
		if(this.status.equals(OrderStatus.CANCELLED) || this.status.equals(OrderStatus.CLOSED) || this.status.equals(OrderStatus.PROCESSING)) {
			// TODO andere bessere Exception
			throw new RuntimeException();
		}
		
		String key = inventory.getClass().getCanonicalName();
		OrderLine o;
		
		if(orderlines.containsKey(key)) {
			orderlines.get(key).addAllSerialNumbers(serialNumbers);
			o = orderlines.get(key);
			this.log.add(new OrderLogEntry("OrderLine: "+o.toString()+" merged succesfully", ""));
		} else {
			o = new OrderLine(inventory, serialNumbers);
			orderlines.put(key,o);
			this.log.add(new OrderLogEntry("OrderLine: "+o.toString()+" added succesfully", ""));
		}
		
		return o;
	}
	
	/**
	 * Adds an <code>OrderLine</code> to this
	 * <code>OrderEntry</code> with defined log entry. The OrderLine cannot be added, 
	 * if this OrderEntry is processing, cancelled or closed.
	 * 
	 * @param orderLine The <code>OrderLine</code> that shall be added.
	 */
	public OrderLine addOrderLine(Inventory<?> inventory, Iterable<SerialNumber> serialNumbers, String logDescription) {
		
		Objects.requireNonNull(inventory, "inventory");
		Objects.requireNonNull(serialNumbers, "serialNumbers");
		if(this.status.equals(OrderStatus.CANCELLED) || this.status.equals(OrderStatus.CLOSED) || this.status.equals(OrderStatus.PROCESSING)) {
			// TODO andere bessere Exception
			throw new RuntimeException();
		}
		
		String key = inventory.getClass().getCanonicalName();
		OrderLine o;
		
		if(orderlines.containsKey(key)) {
			orderlines.get(key).addAllSerialNumbers(serialNumbers);
			o = orderlines.get(key);
			this.log.add(new OrderLogEntry("OrderLine: "+o.toString()+" merged succesfully", logDescription));
		} else {
			o = new OrderLine(inventory, serialNumbers);
			orderlines.put(key,o);
			this.log.add(new OrderLogEntry("OrderLine: "+o.toString()+" added succesfully", logDescription));
		}
		
		return o;
	}
	
	/**
	 * Remove an <code>OrderLine</code> from this
	 * <code>OrderEntry</code> with standard log entry. The OrderLine cannot be removed, 
	 * if this OrderEntry is processing, cancelled or closed.
	 * 
	 * @param id The Identifier from the <code>OrderLine</code> that shall be removed.
	 * 
	 * @return the OrderLine that was removed, or null if nothing is removed.
	 */
	public OrderLine removeOrderLine(OrderLineIdentifier id) {
		
		Objects.requireNonNull(id, "id");
		if(this.status.equals(OrderStatus.CANCELLED) || this.status.equals(OrderStatus.CLOSED) || this.status.equals(OrderStatus.PROCESSING)) return null;
		
		OrderLine lineToRemove = null;
		boolean available = false;
		
		for(OrderLine ol : this.orderlines.values()) {
			if(ol.getIdentifier().equals(id)) {
				lineToRemove = ol;
				available = true;
				break;
			}
		}
		
		if(available == false) return null;
		else {
			OrderLine ret = this.orderlines.remove(lineToRemove.getInventory().getClass().getCanonicalName());
			
			if(ret != null) {
				this.log.add(new OrderLogEntry("OrderLine: "+ret.toString()+" removed succesfully", ""));
			}
			
			return ret;
		}
	}
	
	/**
	 * Remove an <code>OrderLine</code> from this
	 * <code>OrderEntry</code> with defined log entry. The OrderLine cannot be removed, 
	 * if this OrderEntry is processing, cancelled or closed.
	 * 
	 * @param id The Identifier from the <code>OrderLine</code> that shall be removed.
	 * 
	 * @return the OrderLine that was removed, or null if nothing is removed.
	 */
	public OrderLine removeOrderLine(OrderLineIdentifier id, String logDescription) {
		
		Objects.requireNonNull(id, "id");
		if(this.status.equals(OrderStatus.CANCELLED) || this.status.equals(OrderStatus.CLOSED) || this.status.equals(OrderStatus.PROCESSING)) return null;
		
		OrderLine lineToRemove = null;
		boolean available = false;
		
		for(OrderLine ol : this.orderlines.values()) {
			if(ol.getIdentifier().equals(id)) {
				lineToRemove = ol;
				available = true;
				break;
			}
		}
		
		if(available == false) return null;
		else {
			OrderLine ret = this.orderlines.remove(lineToRemove.getInventory().getClass().getCanonicalName());
			
			if(ret != null) {
				this.log.add(new OrderLogEntry("OrderLine: "+ret.toString()+" removed succesfully", logDescription));
			}
			
			return ret;
		}
	}
	
	/**
	 * Change the <code>OrderStatus</code> from this
	 * <code>OrderEntry</code>. Standard log entry is created.
	 * 
	 * @param status The OrderStatus to which the <code>OrderEntry</code> shall be changed.
	 */
	public void changeOrderStatus(OrderStatus orderStatus) {
		
		Objects.requireNonNull(orderStatus, "orderStatus");
		this.status = orderStatus;
		this.log.add(new OrderLogEntry("OrderStatus switched to "+orderStatus.toString(), ""));
		
		switch (orderStatus)
        {
          case CANCELLED:
        	  
        	  for(OrderLine ol : this.orderlines.values()) {
        		  ol.mutableChargeLines = false;
        		  ol.mutableOrderLine = false;
        	  }
        	  break;
        	  
          case CLOSED:
        	  
        	  for(OrderLine ol : this.orderlines.values()) {
        		  ol.mutableChargeLines = false;
        		  ol.mutableOrderLine = false;
        	  }
        	  break;
        	  
          case OPEN:
        	  
        	  for(OrderLine ol : this.orderlines.values()) {
        		  ol.mutableChargeLines = true;
        		  ol.mutableOrderLine = true;
        	  }
        	  break;
        	  
          case INITIALIZED:
        	  
        	  for(OrderLine ol : this.orderlines.values()) {
        		  ol.mutableChargeLines = true;
        		  ol.mutableOrderLine = true;
        	  }
        	  break;
        	  
          case PROCESSING:
        	  
        	  for(OrderLine ol : this.orderlines.values()) {
        		  ol.mutableChargeLines = true;
        		  ol.mutableOrderLine = false;
        	  }
        	  break;
        }
	}
	
	/**
	 * Change the <code>OrderStatus</code> from this
	 * <code>OrderEntry</code>. A log entry with the given description is created.
	 * 
	 * @param status The OrderStatus to which the <code>OrderEntry</code> shall be changed.
	 */
	public void changeOrderStatus(OrderStatus orderStatus, String logDescription) {
		
		Objects.requireNonNull(orderStatus, "orderStatus");
		this.status = orderStatus;
		this.log.add(new OrderLogEntry("OrderStatus switched to "+orderStatus.toString(), logDescription));
		
		switch (orderStatus)
        {
          case CANCELLED:
        	  
        	  for(OrderLine ol : this.orderlines.values()) {
        		  ol.mutableChargeLines = false;
        		  ol.mutableOrderLine = false;
        	  }
        	  break;
        	  
          case CLOSED:
        	  
        	  for(OrderLine ol : this.orderlines.values()) {
        		  ol.mutableChargeLines = false;
        		  ol.mutableOrderLine = false;
        	  }
        	  break;
        	  
          case OPEN:
        	  
        	  for(OrderLine ol : this.orderlines.values()) {
        		  ol.mutableChargeLines = true;
        		  ol.mutableOrderLine = true;
        	  }
        	  break;
        	  
          case INITIALIZED:
        	  
        	  for(OrderLine ol : this.orderlines.values()) {
        		  ol.mutableChargeLines = true;
        		  ol.mutableOrderLine = true;
        	  }
        	  break;
        	  
          case PROCESSING:
        	  
        	  for(OrderLine ol : this.orderlines.values()) {
        		  ol.mutableChargeLines = true;
        		  ol.mutableOrderLine = false;
        	  }
        	  break;
        }
	}

}
