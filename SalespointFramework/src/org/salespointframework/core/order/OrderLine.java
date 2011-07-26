package org.salespointframework.core.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.DateTime;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.core.product.SerialNumber;
import org.salespointframework.core.quantity.Metric;
import org.salespointframework.core.quantity.Quantity;
import org.salespointframework.util.Objects;
import org.salespointframework.util.SalespointIterable;

/**
 * 
 * @author Thomas Dedek
 * 
 */
@Entity
public class OrderLine {
	
	@EmbeddedId
	private OrderLineIdentifier identifier;

	@OneToMany(cascade = CascadeType.ALL)
	private List<ChargeLine> chargeLines;
	
	@Embedded
	@AttributeOverride(name="id", column=@Column(name="PRODUCT_ID"))
	private ProductIdentifier productType;

	@Embedded
	@AttributeOverride(name="id", column=@Column(name="SERIALNO"))
	private SerialNumber serialNumber;

	private String description;
	private String comment;
	private int numberOrdered;

	private Money unitPrice;
	@Temporal(TemporalType.TIMESTAMP)
	private Date expectedDeliveryDate;
	
	protected boolean mutableChargeLines; 

	@Deprecated
	protected OrderLine() {}
	
	public OrderLine(
			// ProductIdentifier productType, SerialNumber serialNumber,
			String description, String comment, int numberOrdered,
			Money unitPrice, DateTime expectedDeliveryDate) {
		this.description = Objects.requireNonNull(description, "description");
		this.comment = Objects.requireNonNull(comment, "comment");
		this.numberOrdered = numberOrdered;
		this.unitPrice = Objects.requireNonNull(unitPrice, "unitPrice");
		this.identifier = new OrderLineIdentifier();
		this.expectedDeliveryDate = Objects.requireNonNull(expectedDeliveryDate, "expectedDeliveryDate").toDate();
		this.chargeLines = new ArrayList<ChargeLine>(); 
		this.mutableChargeLines = true;
		//this.serialNumber = Objects.requireNonNull(serialNumber, "serialNumber");
		//this.productType = Objects.requireNonNull(productType, "productType");
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
		return comment;
	}

	/**
	 * @return the numberOrdered
	 */
	public int getNumberOrdered() {
		return numberOrdered;
	}

	/**
	 * @return the unitPrice
	 */
	public Money getUnitPrice() {
		return unitPrice;
	}
	
	/**
	 * Calculates the total price of this OrderLine. The number of ordered objects and ChargeLines are included in the calculation.
	 * 
	 * @return the total price of this OrderLine
	 */
	public Money getOrderLinePrice() {
		Money price = new Money(this.unitPrice.getAmount(), this.unitPrice.getMetric());
		//price = (Money) price.multiply(new Money(this.numberOrdered));
		price = new Quantity(this.numberOrdered, Metric.PIECES, Quantity.ROUND_ONE).multiply_(price);
		
		for(ChargeLine cl : this.chargeLines) {
			price = (Money) price.add(cl.getAmount());
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
	 * Increments the number of the ordered objects in this OrderLine. 
	 * This method doesn't change anything, if the given number is less than or equal to 0.
	 * 
	 * @param number
	 *            the number of ordered objects that shall to be added.  
	 */
	public void incrementNumberOrdered(int number) {
		
		if(number <= 0) return;
		this.numberOrdered += number;
	}
	
	/**
	 * Decrements the number of the ordered objects in this OrderLine. 
	 * This method doesn't change anything, if the given number is less than or equal to 0.
	 * The number of ordered objects cannot fall below 0.
	 * 
	 * @param number
	 *            the number of ordered objects that shall to be substituted.  
	 */
	public void decrementNumberOrdered(int number) {
		
		if(number <= 0) return;
		if(number > this.numberOrdered) this.numberOrdered = 0;
		else {
			this.numberOrdered -= number;
		}
		
		return;
	}
	
	/**
	 * Add a <code>ChargeLine</code> to this
	 * <code>OrderLine</code>. The OrderLine cannot be added, 
	 * if this OrderLine is provided in the context of an cancelled or closed OrderEntry.
	 * 
	 * @param chargeLine The <code>ChargeLine</code> that shall be added.
	 */
	public boolean addChargeLine(ChargeLine chargeLine) {
		
		Objects.requireNonNull(chargeLine, "chargeLine");
		if(!this.mutableChargeLines) return false;
		
		return this.chargeLines.add(chargeLine);
	}
	
	/**
	 * Remove a <code>ChargeLine</code> from this
	 * <code>OrderLine</code>. The OrderLine cannot be removed, 
	 * if this OrderLine is provided in the context of an cancelled or closed OrderEntry.
	 * 
	 * @param id The Identifier from the <code>ChargeLine</code> that shall be removed.
	 */
	public boolean removeChargeLine(OrderLineIdentifier id) {
		
		Objects.requireNonNull(id, "id");
		if(!this.mutableChargeLines) return false;
		
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
	
	
	@Override
    public boolean equals(Object obj) {
		
        if (obj == this) return true;
        if (obj == null) return false;
        if (!(obj instanceof OrderLine)) return false;
        
        OrderLine ol = (OrderLine) obj;
        return this.getIdentifier().equals(ol.getIdentifier());
    }
}
