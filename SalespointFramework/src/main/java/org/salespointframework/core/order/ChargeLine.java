package org.salespointframework.core.order;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;

import org.salespointframework.core.AbstractEntity;
import org.salespointframework.core.money.Money;

import java.util.Objects;

/**
 * A chargeline represents extra expenses like shipping.
 * This class is immutable.
 * 
 * @author Thomas Dedek
 * @author Paul Henke
 * 
 */
@Entity
public class ChargeLine extends AbstractEntity<ChargeLineIdentifier> 
{
	
	@EmbeddedId
	@AttributeOverride(name = "id", column = @Column(name = "CHARGELINE_ID"))
	private ChargeLineIdentifier chargeLineIdentifier = new ChargeLineIdentifier();

	@Lob
	private final Money amount;
	private final String description;

	/**
	 * Parameterless constructor required for JPA. Do not use.
	 */
	@Deprecated
	protected ChargeLine()
	{
		amount = null;
		description = null;
	}

	/**
	 * Creates a new PersistentChargeLine
	 * @param amount the value of the ChargeLine
	 * @param description a description of the ChargeLine
	 * @throws NullPointerException if amout or description is null
	 */
	public ChargeLine(Money amount, String description)
	{
		this.amount = Objects.requireNonNull(amount, "amount must not be null");
		this.description = Objects.requireNonNull(description, "description must not be null");
	}

	/**
	 *  
	 * @return the value of the chargeline
	 */

	public Money getPrice()
	{
		return amount;
	}

	/**
	 * 
	 * @return the description of the chargeline
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @return the {@link ChargeLineIdentifier} to uniquely identify this chargeline
	 */
	public ChargeLineIdentifier getIdentifier()
	{
		return chargeLineIdentifier;
	}

	@Override
	public String toString()
	{
		return "Amount: " + amount.toString() + "| Description:" + description;
	}
}
