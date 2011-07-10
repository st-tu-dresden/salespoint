package org.salespointframework.core.order;

import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.salespointframework.core.money.Money;
import org.salespointframework.util.Objects;

/**
 * 
 * @author hannesweisbach
 * 
 */
@Entity
public class ChargeLine {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private Money amount;
	private String description;
	private String comment;
	private OrderLineIdentifier identifier;

	@Deprecated
	protected ChargeLine() {}
	
	public ChargeLine(OrderLineIdentifier identifier, Money amount,
			String description, String comment) {
		this.identifier = Objects.requireNonNull(identifier, "identifier");
		this.amount = Objects.requireNonNull(amount, "amount");
		this.description = Objects.requireNonNull(description, "description");
		this.comment = Objects.requireNonNull(comment, "comment");
	}

	public ChargeLine(OrderLineIdentifier identifier, Money amount, String description) {
		this(Objects.requireNonNull(identifier, "identifier"), Objects.requireNonNull(amount, "amount"), Objects.requireNonNull(
				description, "description"), "");
	}

	public ChargeLine(OrderLineIdentifier identifier, Money amount) {
		this(Objects.requireNonNull(identifier, "identifier"), Objects.requireNonNull(amount, "amount"), "", "");
	}

	public ChargeLine(OrderLineIdentifier identifier) {
		this(Objects.requireNonNull(identifier, "identifier"), new Money(0), "", "");
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
