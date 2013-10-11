package org.salespointframework.core.accountancy;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.DateTime;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.shop.Shop;
import java.util.Objects;

/**
 * This class represents an accountancy entry. The
 * <code>PersistentAccountancyEntry</code> may be used directly, but it is
 * advisable to sub-class it, to define specific entry types for an accountancy,
 * for example a {@link ProductPaymentEntry}.
 * 
 * @author Hannes Weisbach
 * 
 */
@Entity
//@Customizer(PersistentAccountancyEntryDescriptorCustomizer.class)
public class AccountancyEntry {

	// TODO: if the column is not renamed, it does not work. instead,
	// ProductPaymentEntry's USER_ID column becomes PK. This fucks everything
	// up, if a PersistentAccountancyEntry is retrieved from the database,
	// because its "PK" (USER_ID) would be NULL. Renaming keeps ENTRY_ID the PK.
	// IdClass annotation did not work. Maybe Using a DescriptorCustomizer can
	// be used, to correct the mappings (don't forget to sacrifice a chicken, if
	// it does).
	@EmbeddedId
	@AttributeOverride(name = "id", column = @Column(name = "ENTRY_ID", nullable = false))
	private AccountancyEntryIdentifier accountancyEntryIdentifier = new AccountancyEntryIdentifier();

	@Lob
	private Money value = Money.ZERO;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date = Shop.INSTANCE.getTime().getDateTime().toDate();

	private String description = "";

	/**
	 * Protected, parameterless Constructor required by the persistence layer.
	 * Do not use it.
	 */
	@Deprecated
	protected AccountancyEntry() {
	}

	/**
	 * Creates a new <code>PersistentAccountancyEntry</code> with a specific
	 * value.
	 * 
	 * @param value
	 *            The value that is stored in this entry.
	 */
	public AccountancyEntry(Money value) {
		this(value, "");
	}

	/**
	 * Creates a new <code>PersistentAccountancyEntry</code> with a specific
	 * value and a user defined time.
	 * 
	 * @param value
	 *            The value that is stored in this entry.
	 * @param description
	 *            A user-supplied description for this entry.
	 */
	public AccountancyEntry(Money value, String description) {
		this.value = Objects.requireNonNull(value, "value must not be null");
		this.description = Objects.requireNonNull(description, "description must not be null");
	}

	public final DateTime getDate() {
		return new DateTime(date);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * The Money object is always non-<code>null</code>.
	 */
	public final Money getValue() {
		return value;
	}

	public final String getDescription() {
		return description;
	}

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
		if (other instanceof AccountancyEntry)
		{
			return this.accountancyEntryIdentifier.equals(((AccountancyEntry)other).accountancyEntryIdentifier);
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
