package org.salespointframework.accountancy;

import java.time.LocalDateTime;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;

import org.hibernate.annotations.Type;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.salespointframework.core.AbstractEntity;
import org.salespointframework.order.ProductPaymentEntry;
import org.springframework.util.Assert;

/**
 * This class represents an accountancy entry. It is advisable to sub-class it, to define specific entry types for an
 * accountancy, for example a {@link ProductPaymentEntry}.
 * 
 * @author Hannes Weisbach
 * @author Oliver Gierke
 */
@Entity
public class AccountancyEntry extends AbstractEntity<AccountancyEntryIdentifier> {

	// TODO: if the column is not renamed, it does not work. instead,
	// ProductPaymentEntry's USER_ID column becomes PK. This fucks everything
	// up, if a PersistentAccountancyEntry is retrieved from the database,
	// because its "PK" (USER_ID) would be NULL. Renaming keeps ENTRY_ID the PK.
	// IdClass annotation did not work. Maybe Using a DescriptorCustomizer can
	// be used, to correct the mappings (don't forget to sacrifice a chicken, if
	// it does).
	@EmbeddedId @AttributeOverride(name = "id", column = @Column(name = "ENTRY_ID", nullable = false)) private AccountancyEntryIdentifier accountancyEntryIdentifier = new AccountancyEntryIdentifier();

	@Lob private Money value = Money.zero(CurrencyUnit.EUR);

	@Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")//
	private LocalDateTime date = null;

	private String description = "";

	/**
	 * Protected, parameterless Constructor required by the persistence layer. Do not use it.
	 */
	@Deprecated
	protected AccountancyEntry() {}

	/**
	 * Creates a new <code>PersistentAccountancyEntry</code> with a specific value.
	 * 
	 * @param value The value that is stored in this entry.
	 */
	public AccountancyEntry(Money value) {
		this(value, "");
	}

	/**
	 * Creates a new <code>PersistentAccountancyEntry</code> with a specific value and a user defined time.
	 * 
	 * @param value The value that is stored in this entry.
	 * @param description A user-supplied description for this entry.
	 */
	public AccountancyEntry(Money value, String description) {

		Assert.notNull(value, "Value must not be null");
		Assert.notNull(description, "Description must not be null");

		this.value = value;
		this.description = description;
	}

	/**
	 * Returns whether the {@link AccountancyEntry} already has a {@link Date} set.
	 * 
	 * @return
	 */
	public boolean hasDate() {
		return date != null;
	}

	/**
	 * @return the {@link DateTime} when this entry was posted.
	 */
	public final LocalDateTime getDate() {
		return date;
	}

	void setDate(LocalDateTime dateTime) {
		this.date = dateTime;
	}

	/**
	 * The Money object is always non-{@literal null}.
	 * 
	 * @return the monetary value for this entry.
	 */
	public final Money getValue() {
		return value;
	}

	/**
	 * @return description, detailing the entry.
	 */
	public final String getDescription() {
		return description;
	}

	/**
	 * @return {@link AccountancyEntryIdentifier} to uniquely identify this entry.
	 */
	@Override
	public final AccountancyEntryIdentifier getIdentifier() {
		return accountancyEntryIdentifier;
	}

	@Override
	public String toString() {
		return value.toString() + " | " + date.toString() + " | " + description;
	}

}
