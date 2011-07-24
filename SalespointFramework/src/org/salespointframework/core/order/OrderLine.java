package org.salespointframework.core.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.DateTime;
import org.salespointframework.core.money.Money;
import org.salespointframework.util.Objects;

/**
 * 
 * @author Thomas Dedek
 * 
 */
@Entity
public class OrderLine {
	
	@Id
	private OrderLineIdentifier identifier;

	@ElementCollection
	private List<ChargeLine> chargeLines;
	// private ProductIdentifier productType;
	// private SerialNumber serialNumber;
	private String description;
	private String comment;
	private int numberOrdered;
	private Money unitPrice;
	@Temporal(TemporalType.TIMESTAMP)
	private Date expectedDeliveryDate;

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
	 * @return the chargeLines
	 */
	public List<ChargeLine> getChargeLines() {
		return chargeLines;
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
}
