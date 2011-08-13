package org.salespointframework.core.order;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.salespointframework.core.money.Money;
import org.salespointframework.util.Objects;

/**
 * 
 * @author Thomas Dedek
 * 
 */
@Entity
public class ChargeLine {
	//@Id @GeneratedValue(strategy=GenerationType.AUTO) private long id;
	
	@EmbeddedId
	@AttributeOverride(name="id", column=@Column(name="CHARGELINE_ID"))
	private OrderLineIdentifier identifier;

	private Money amount;
	private String description;
	private String comment;
	
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
	
	@Override
	public boolean equals(Object other) {
		if (other == this)
			return true;
		if (other == null)
			return false;
		if (!(other instanceof ChargeLine))
			return false;
		return this.equals((ChargeLine) other);
	}

	public boolean equals(ChargeLine other) {
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
