package org.salespointframework.core.order;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
	
	//TODO
	//private List<OrderAction> orderActions;
	
	@OneToMany(cascade = CascadeType.ALL)
	private Set<ChargeLine> chargeLines = new HashSet<ChargeLine>();
	@OneToMany(cascade = CascadeType.ALL)
	private Map<String, OrderLine> paul_orderlines = new HashMap<String, OrderLine>();
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	
	public OrderEntry(String salesChannel, String termsAndConditions) {
		orderIdentifier = new OrderIdentifier();
		dateCreated = Shop.INSTANCE.getTime().getDateTime().toDate();
		this.salesChannel = Objects
				.requireNonNull(salesChannel, "salesChannel");
		this.termsAndConditions = Objects.requireNonNull(termsAndConditions,
				"termsAndConditions");
		//TODO
		//orderActions = new ArrayList<OrderAction>();
		status = OrderStatus.INITIALIZED;
	}

	
	// PAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAUL

	
	public OrderLine addOrderLine(OrderLine orderLine) {
		Objects.requireNonNull(orderLine, "orderLine");
		if(this.status.equals(OrderStatus.CANCELLED) || this.status.equals(OrderStatus.CLOSED) || this.status.equals(OrderStatus.PROCESSING)) {
			// TODO andere bessere Exception
			throw new RuntimeException();
		}
		
		String key = orderLine.getInventory().getClass().getCanonicalName();
		OrderLine o = orderLine;
		
		if(paul_orderlines.containsKey(key)) {
			 paul_orderlines.get(key).mergeOrderLine(o);
		}
		paul_orderlines.put(key, o);
		return paul_orderlines.get(key);
	}
	
	public OrderLine addOrderLine(Inventory<?> inventory, SerialNumber serialNumber) {
		
		String key = inventory.getClass().getCanonicalName();
		OrderLine o;
		
		if(paul_orderlines.containsKey(key)) {
			paul_orderlines.get(key).addSerialNumber(serialNumber);
			o = paul_orderlines.get(key);
		} else {
			o = new OrderLine(inventory, serialNumber);
			paul_orderlines.put(key,o);
		}
		
		return o;
	}
	
	public OrderLine addOrderLine(Inventory<?> inventory, Iterable<SerialNumber> serialNumbers) {
		String key = inventory.getClass().getCanonicalName();
		OrderLine o;
		
		if(paul_orderlines.containsKey(key)) {
			paul_orderlines.get(key).addAllSerialNumbers(serialNumbers);
			o = paul_orderlines.get(key);
		} else {
			o = new OrderLine(inventory, serialNumbers);
			paul_orderlines.put(key,o);
		}
		
		return o;
	}

	
	public boolean paul_completeOrder() {
		EntityManager em = Database.INSTANCE.getEntityManagerFactory().createEntityManager();
		
		em.getTransaction().begin();
		
		for(Map.Entry<String, OrderLine> entry : paul_orderlines.entrySet()) {
			String key = entry.getKey();
			OrderLine orderLine = entry.getValue();
			
			// I FUCKING HATE CHECKED EXCEPTIONS
			// I FUCKING HATE CHECKED EXCEPTIONS
			// I FUCKING HATE CHECKED EXCEPTIONS
			// I FUCKING HATE CHECKED EXCEPTIONS
			// I FUCKING HATE CHECKED EXCEPTIONS
			
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
	
	// PAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAUL
	
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
	//TODO
/*	public Iterable<OrderAction> getOrderAction() {
		return SalespointIterable.from(this.orderActions);
	}*/
	
	/**
	 * @return the orderLines
	 */
	public Iterable<OrderLine> getOrderLines() {
		return SalespointIterable.from(this.paul_orderlines.values());
	}
	
	/**
	 * @return an Iterable with all chargeLines from this OrderEntry
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
		
		if(!this.paul_orderlines.isEmpty()) {
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
		
		for(OrderLine ol : this.paul_orderlines.values()) {
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
	 * <code>OrderEntry</code>. The ChargeLine cannot be added, 
	 * if this OrderEntry is cancelled or closed.
	 * 
	 * @param chargeLine The <code>ChargeLine</code> that shall be added.
	 */
	public boolean addChargeLine(ChargeLine chargeLine) {
		
		Objects.requireNonNull(chargeLine, "chargeLine");
		if(this.status.equals(OrderStatus.CANCELLED) || this.status.equals(OrderStatus.CLOSED)) return false;
		
		return this.chargeLines.add(chargeLine);
	}
	
	/**
	 * Remove a <code>ChargeLine</code> from this
	 * <code>OrderEntry</code>. The ChargeLine cannot be removed, 
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
		else return this.chargeLines.remove(lineToRemove);
	}
	
	/**
	 * Add an <code>OrderLine</code> to this
	 * <code>OrderEntry</code>. The OrderLine cannot be added, 
	 * if this OrderEntry is processing, cancelled or closed.
	 * 
	 * @param orderLine The <code>OrderLine</code> that shall be added.
	 */
	/*
	public boolean addOrderLine(OrderLine orderLine) {
		
		Objects.requireNonNull(orderLine, "orderLine");
		if(this.status.equals(OrderStatus.CANCELLED) || this.status.equals(OrderStatus.CLOSED) || this.status.equals(OrderStatus.PROCESSING)) return false;
		
		return this.orderLines.add(orderLine);
	}
	*/
	
	/**
	 * Remove an <code>OrderLine</code> from this
	 * <code>OrderEntry</code>. The OrderLine cannot be removed, 
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
		
		for(OrderLine ol : this.paul_orderlines.values()) {
			if(ol.getIdentifier().equals(id)) {
				lineToRemove = ol;
				available = true;
				break;
			}
		}
		
		if(available == false) return null;
		else return this.paul_orderlines.remove(lineToRemove.getInventory().getClass().getCanonicalName());
	}
	
	/**
	 * Change the <code>OrderStatus</code> from this
	 * <code>OrderEntry</code>.
	 * 
	 * @param status The OrderStatus to which the <code>OrderEntry</code> shall be changed.
	 */
	public void changeOrderStatus(OrderStatus orderStatus) {
		
		Objects.requireNonNull(orderStatus, "orderStatus");
		this.status = orderStatus;
		
		switch (orderStatus)
        {
          case CANCELLED:
        	  
        	  for(OrderLine ol : this.paul_orderlines.values()) {
        		  ol.mutableChargeLines = false;
        		  ol.mutableOrderLine = false;
        	  }
        	  break;
        	  
          case CLOSED:
        	  
        	  for(OrderLine ol : this.paul_orderlines.values()) {
        		  ol.mutableChargeLines = false;
        		  ol.mutableOrderLine = false;
        	  }
        	  break;
        	  
          case OPEN:
        	  
        	  for(OrderLine ol : this.paul_orderlines.values()) {
        		  ol.mutableChargeLines = true;
        		  ol.mutableOrderLine = true;
        	  }
        	  break;
        	  
          case INITIALIZED:
        	  
        	  for(OrderLine ol : this.paul_orderlines.values()) {
        		  ol.mutableChargeLines = true;
        		  ol.mutableOrderLine = true;
        	  }
        	  break;
        	  
          case PROCESSING:
        	  
        	  for(OrderLine ol : this.paul_orderlines.values()) {
        		  ol.mutableChargeLines = true;
        		  ol.mutableOrderLine = false;
        	  }
        	  break;
        }
	}
	
	//TODO Complete Order and remove Objects from Inventory
	//TODO Return Iterable of serialNumbers
	public boolean completeOrder() {
		return false;
	}

}
