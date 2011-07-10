package org.salespointframework.core.order;

import java.util.Date;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;

import org.joda.time.DateTime;
import org.salespointframework.core.money.Money;
import org.salespointframework.util.Objects;

/**
 * 
 * @author hannesweisbach
 * 
 */
@Entity
public class OrderLine {
	@Id
	private OrderLineIdentifier identifier;

	@OneToMany
	private List<ChargeLine> chargeLines;
	// private ProductIdentifier productType;
	// private SerialNumber serialNumber;
	private String description;
	private String comment;
	private int numberOrdered;
	private Money unitPrice;
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
		this.expectedDeliveryDate = Objects.requireNonNull(
				expectedDeliveryDate, "expectedDeliveryDate").toDate();
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
	
}
