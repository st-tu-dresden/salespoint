package org.salespointframework.core.order;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.salespointframework.core.money.Money;
import org.salespointframework.util.Objects;

/**
 * 
 * @author Thomas Dedek
 * @author Paul Henke
 * 
 */
@Entity
public class PersistentChargeLine implements ChargeLine
{
	@EmbeddedId
	private ChargeLineIdentifier chargeLineIdentifier;

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

	public PersistentChargeLine(Money amount, String description)
	{
		this(amount, description, "");
	}

	public PersistentChargeLine(Money amount, String description, String comment)
	{
		this.amount = Objects.requireNonNull(amount, "amount");
		this.description = Objects.requireNonNull(description, "description");
		this.comment = Objects.requireNonNull(comment, "comment");

		this.chargeLineIdentifier = new ChargeLineIdentifier();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Money getPrice()
	{
		return amount;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDescription()
	{
		return description;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getComment()
	{
		return comment;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChargeLineIdentifier getChargeLineIdentifier()
	{
		return chargeLineIdentifier;
	}

	/**
	 * {@inheritDoc}
	 */
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
		if (!(other instanceof PersistentChargeLine))
		{
			return false;
		}
		return this.equals((PersistentChargeLine) other);
	}

	/**
	 * {@inheritDoc}
	 */
	public final boolean equals(PersistentChargeLine other)
	{
		if (other == null)
		{
			return false;
		}
		if (other == this)
		{
			return true;
		}
		return this.chargeLineIdentifier.equals(other.chargeLineIdentifier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int hashCode()
	{
		return this.chargeLineIdentifier.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "Amount: " + amount.toString() + "| Description:" + description;
	}
}
