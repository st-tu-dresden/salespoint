package org.salespointframework.core.order;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.salespointframework.core.money.Money;
import org.salespointframework.util.Objects;

/**
 * 
 * @author Thomas Dedek
 * 
 */
@Entity
public class ChargeLine {
	
	@Id
	private OrderLineIdentifier identifier;

	private Money amount;
	private String description;
	private String comment;
	//private OrderLineIdentifier identifier;
	
	@Deprecated
	protected ChargeLine() {}
	
	public ChargeLine(Money amount, String description, String comment) {
		this.identifier = new OrderLineIdentifier();
		this.amount = Objects.requireNonNull(amount, "amount");
		this.description = Objects.requireNonNull(description, "description");
		this.comment = Objects.requireNonNull(comment, "comment");
	}

	public ChargeLine(Money amount, String description) {
		this(Objects.requireNonNull(amount, "amount"), Objects.requireNonNull(
				description, "description"), "");
	}

	/**
	 * @return the amount
	 */
	public Money getAmount() {
		return amount;
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
	 * @return the identifier
	 */
	public OrderLineIdentifier getIdentifier() {
		return identifier;
	}
}
