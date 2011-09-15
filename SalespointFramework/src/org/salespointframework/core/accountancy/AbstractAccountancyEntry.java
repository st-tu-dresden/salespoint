package org.salespointframework.core.accountancy;

import java.util.Date;

import javax.persistence.*;

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
public abstract class AbstractAccountancyEntry implements AccountancyEntry {

    /**
     * Identifier of this entry. Only used to persist to database.
     */
	@EmbeddedId
    private AccountancyEntryIdentifier accountancyEntryIdentifier;

	/**
	 * {@inheritDoc}
	 */
    @Override
	public AccountancyEntryIdentifier getAccountancyEntryIdentifier() {
		return accountancyEntryIdentifier;
	}

	/**
     * Represents the value of this entry.
     */
    private Money amount;

    /**
     * The date at which this entry has been created or that was given as a
     * constructor parameter.
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    /**
     * Protected, parameterless Constructor required by the persistence layer.
     * Do not use it.
     */
    @Deprecated
    protected AbstractAccountancyEntry() {
        dateCreated = Shop.INSTANCE.getTime().getDateTime().toDate();
        this.amount = Money.ZERO;
    }

    /**
     * Creates a new <code>AbstractAccountancyEntry</code> with a specific
     * value.
     * 
     * @param amount
     *            The value that is stored in this entry.
     */
    public AbstractAccountancyEntry(Money amount) {
        this();
        this.amount = Objects.requireNonNull(amount, "amount");
    }

    /**
     * Creates a new <code>AbstractAccountancyEntry</code> with a specifc value
     * and a user defined time.
     * 
     * @param amount The value that is stored in this entry.
     * @param timeStamp A user defined timestamp for this entry.
     */
    public AbstractAccountancyEntry(Money amount, DateTime timeStamp) {
        this.amount = Objects.requireNonNull(amount, "amount");
        this.dateCreated = Objects.requireNonNull(timeStamp, "timeStamp").toDate();
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
	public DateTime getCreationDate() {
        return new DateTime(dateCreated);
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
	public Money getValue() {
        return amount;
    }
}
