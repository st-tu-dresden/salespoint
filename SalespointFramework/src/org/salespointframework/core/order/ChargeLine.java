package org.salespointframework.core.order;

import javax.persistence.Embeddable;

import org.salespointframework.core.money.Money;
import org.salespointframework.util.ArgumentNullException;
import org.salespointframework.util.Objects;

// TODO comment
/**
 * 
 * This class is immutable.
 * 
 * @author Thomas Dedek
 * @author Paul Henke
 * 
 */
@Embeddable
public final class ChargeLine
{
	private final ChargeLineIdentifier chargeLineIdentifier = new ChargeLineIdentifier();

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
	 * @throws ArgumentNullException if amout or description is null
	 */
	public ChargeLine(Money amount, String description)
	{
		this.amount = Objects.requireNonNull(amount, "amount");
		this.description = Objects.requireNonNull(description, "description");
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
		if (other instanceof ChargeLine)
		{
			return this.chargeLineIdentifier.equals(((ChargeLine)other).chargeLineIdentifier);
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
