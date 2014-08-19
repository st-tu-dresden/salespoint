package org.salespointframework.order;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;

import org.joda.money.Money;
import org.salespointframework.core.AbstractEntity;
import org.springframework.util.Assert;

/**
 * A chargeline represents extra expenses like shipping. This class is immutable.
 * 
 * @author Thomas Dedek
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Entity
public class ChargeLine extends AbstractEntity<ChargeLineIdentifier> implements Priced {

	@EmbeddedId//
	@AttributeOverride(name = "id", column = @Column(name = "CHARGELINE_ID"))//
	private ChargeLineIdentifier chargeLineIdentifier = new ChargeLineIdentifier();

	private final @Lob Money amount;
	private final String description;

	/**
	 * Parameterless constructor required for JPA. Do not use.
	 */
	@Deprecated
	protected ChargeLine() {

		this.amount = null;
		this.description = null;
	}

	/**
	 * Creates a new PersistentChargeLine with the given amount and description.
	 * 
	 * @param amount the value of the ChargeLine
	 * @param description a description of the ChargeLine
	 * @throws NullPointerException if amout or description is null
	 */
	public ChargeLine(Money amount, String description) {

		Assert.notNull(amount, "Amount must not be null");
		Assert.notNull(description, "Description must not be null");

		this.amount = amount;
		this.description = description;
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.order.Priced#getPrice()
	 */
	public Money getPrice() {
		return amount;
	}

	/**
	 * @return the description of the chargeline
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the {@link ChargeLineIdentifier} to uniquely identify this chargeline
	 */
	public ChargeLineIdentifier getIdentifier() {
		return chargeLineIdentifier;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Amount: " + amount.toString() + "| Description:" + description;
	}
}
