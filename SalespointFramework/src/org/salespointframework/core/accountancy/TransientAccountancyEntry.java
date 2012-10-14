package org.salespointframework.core.accountancy;

import java.util.Objects;

import org.joda.time.DateTime;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.shop.Shop;

public class TransientAccountancyEntry implements AccountancyEntry
{
	private final AccountancyEntryIdentifier accountancyEntryIdentifier = new AccountancyEntryIdentifier();

	private Money value = Money.ZERO;

	private final DateTime date = Shop.INSTANCE.getTime().getDateTime();

	private String description = "";


	/**
	 * Creates a new <code>TransientAccountancyEntry</code> with a specific
	 * value.
	 * 
	 * @param value
	 *            The value that is stored in this entry.
	 */
	public TransientAccountancyEntry(Money value) {
		this(value, "");
	}

	/**
	 * Creates a new <code>TransientAccountancyEntry</code> with a specific
	 * value and a user defined time.
	 * 
	 * @param value
	 *            The value that is stored in this entry.
	 * @param description
	 *            A user-supplied description for this entry.
	 */
	public TransientAccountancyEntry(Money value, String description) {
		this.value = Objects.requireNonNull(value, "value must not be null");
		this.description = Objects.requireNonNull(description, "description must not be null");
	}

	@Override
	public final DateTime getDate() {
		return date;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * The Money object is always non-<code>null</code>.
	 */
	@Override
	public final Money getValue() {
		return value;
	}

	@Override
	public final String getDescription() {
		return description;
	}

	@Override
	public final AccountancyEntryIdentifier getIdentifier() {
		return accountancyEntryIdentifier;
	}
	
	@Override
	public final boolean equals(Object other)
	{
		if (other == null)
		{
			return false;
		}
		if (other == this)
		{
			return true;
		}
		if (other instanceof PersistentAccountancyEntry)
		{
			return this.accountancyEntryIdentifier.equals(((TransientAccountancyEntry)other).accountancyEntryIdentifier);
		}
		return false;
	}

	@Override
	public final int hashCode()
	{
		return accountancyEntryIdentifier.hashCode();
	}

	@Override
	public String toString() {
		return value.toString() + " | " + date.toString() + " | " + description;
	}
}
