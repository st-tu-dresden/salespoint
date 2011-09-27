package org.salespointframework.core.accountancy;

import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.DateTime;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.shop.Shop;
import org.salespointframework.util.Objects;

/**
 * This class represents an accountancy entry. The
 * <code>AbstractAccountancyEntry</code> is not intended to be directly used.
 * Instead, it should be sub-classed to define specific entry types for an
 * accountancy, for example a <code>ProductPaymentEntry</code>.
 * 
 * @author Hannes Weisbach
 * 
 */
@Entity
public class PersistentAccountancyEntry implements AccountancyEntry {

	@EmbeddedId
	private AccountancyEntryIdentifier accountancyEntryIdentifier;

	private Money value;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	private String description;

	/**
	 * Protected, parameterless Constructor required by the persistence layer.
	 * Do not use it.
	 */
	@Deprecated
	protected PersistentAccountancyEntry() {
		accountancyEntryIdentifier = new AccountancyEntryIdentifier();
		date = Shop.INSTANCE.getTime().getDateTime().toDate();
		this.value = Money.ZERO;
	}

	/**
	 * Creates a new <code>AbstractAccountancyEntry</code> with a specific
	 * value.
	 * 
	 * @param value
	 *            The value that is stored in this entry.
	 */
	public PersistentAccountancyEntry(Money value) {
		this();
		this.value = Objects.requireNonNull(value, "value");
	}

	/**
	 * Creates a new <code>AbstractAccountancyEntry</code> with a specific value
	 * and a user defined time.
	 * 
	 * @param value
	 *            The value that is stored in this entry.
	 * @param date
	 *            A user defined timestamp for this entry.
	 */
	public PersistentAccountancyEntry(Money value, DateTime date) {
		this.value = Objects.requireNonNull(value, "value");
		this.date = Objects.requireNonNull(date, "date").toDate();
	}

	@Override
	public DateTime getDate() {
		return new DateTime(date);
	}

	@Override
	public Money getValue() {
		return value;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public AccountancyEntryIdentifier getAccountancyEntryIdentifier() {
		return accountancyEntryIdentifier;
	}

}
