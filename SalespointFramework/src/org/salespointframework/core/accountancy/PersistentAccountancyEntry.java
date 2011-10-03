package org.salespointframework.core.accountancy;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.DateTime;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.PersistentProduct;
import org.salespointframework.core.shop.Shop;
import org.salespointframework.util.Objects;

/**
 * This class represents an accountancy entry. The
 * <code>PersistentAccountancyEntry</code> may be used directly, but it is
 * advisable to sub-class it, to define specific entry types for an accountancy,
 * for example a <code>ProductPaymentEntry</code>.
 * 
 * @author Hannes Weisbach
 * 
 */
@Entity
//@Customizer(PersistentAccountancyEntryDescriptorCustomizer.class)
public class PersistentAccountancyEntry implements AccountancyEntry {

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

	private Money value = Money.ZERO;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date = Shop.INSTANCE.getTime().getDateTime().toDate();

	private String description = "";

	/**
	 * Protected, parameterless Constructor required by the persistence layer.
	 * Do not use it.
	 */
	@Deprecated
	protected PersistentAccountancyEntry() {
	}

	/**
	 * Creates a new <code>PersistentAccountancyEntry</code> with a specific
	 * value.
	 * 
	 * @param value
	 *            The value that is stored in this entry.
	 */
	public PersistentAccountancyEntry(Money value) {
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
	public PersistentAccountancyEntry(Money value, String description) {
		this.value = Objects.requireNonNull(value, "value");
		this.description = Objects.requireNonNull(description, "description");
	}

	@Override
	public DateTime getDate() {
		return new DateTime(date);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * The Money object is always non-<code>null</code>.
	 */
	@Override
	public Money getValue() {
		return value;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public AccountancyEntryIdentifier getIdentifier() {
		return accountancyEntryIdentifier;
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
		if (other instanceof PersistentProduct)
		{
			return this.accountancyEntryIdentifier.equals(((PersistentAccountancyEntry)other).accountancyEntryIdentifier);
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
