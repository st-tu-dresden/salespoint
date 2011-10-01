package org.salespointframework.core.order;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.salespointframework.core.money.Money;
import org.salespointframework.util.Objects;

/**
 * TODO
 * @author Thomas Dedek
 * @author Paul Henke
 * 
 */
@Entity
public class PersistentChargeLine implements ChargeLine
{
	@EmbeddedId
	private ChargeLineIdentifier chargeLineIdentifier = new ChargeLineIdentifier();

	private Money amount;
	private String description;
	private String comment;

	/**
	 * Parameterless constructor required for JPA. Do not use.
	 */
	@Deprecated
	protected PersistentChargeLine()
	{
	}

	/**
	 * Creates a new PersistentChargeLine
	 * @param amount the value of the ChargeLine
	 * @param description a description
	 */
	public PersistentChargeLine(Money amount, String description)
	{
		this(amount, description, "");
	}

	/**
	 * Creates a new PersistentChargeLine
	 * @param amount the value of the ChargeLine
	 * @param description a description
	 * @param comment a comment
	 */
	public PersistentChargeLine(Money amount, String description, String comment)
	{
		this.amount = Objects.requireNonNull(amount, "amount");
		this.description = Objects.requireNonNull(description, "description");
		this.comment = Objects.requireNonNull(comment, "comment");
	}

	@Override
	public Money getPrice()
	{
		return amount;
	}

	@Override
	public String getDescription()
	{
		return description;
	}

	@Override
	public String getComment()
	{
		return comment;
	}

	@Override
	public ChargeLineIdentifier getIdentifier()
	{
		return chargeLineIdentifier;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other == null)
		{
			return false;
		}
		if (other == this)
		{
			return true;
		}
		if (other instanceof PersistentChargeLine)
		{
			return this.chargeLineIdentifier.equals(((PersistentChargeLine)other).chargeLineIdentifier);
		}
		return false;
	}

	@Override
	public final int hashCode()
	{
		return this.chargeLineIdentifier.hashCode();
	}

	@Override
	public String toString()
	{
		return "Amount: " + amount.toString() + "| Description:" + description;
	}
}
