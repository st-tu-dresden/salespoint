package org.salespointframework.core.accountancy;

import java.util.Date;

import javax.persistence.*;

import org.joda.time.DateTime;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.shop.Shop;
import org.salespointframework.util.Objects;

/**
 * This class represents an accountancy entries. The
 * <code>AbstractAccountancyEntry</code> is not intended to be directly used. Instead,
 * it should be sub-classed to define specific entry types for an accountancy,
 * for example a <code>ProductPaymentEntry</code>.
 * 
 * @author hannesweisbach
 * 
 */
@Entity
public abstract class AbstractAccountancyEntry {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@SuppressWarnings("unused")
	private long identifier;

	private Money amount;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date timeStamp;
	
	/**
	 * Protected, parameterless Constructor required by the persistence layer.
	 * Do not use it.
	 */
	protected AbstractAccountancyEntry() {
		timeStamp = Shop.INSTANCE.getTime().getDateTime().toDate();
		this.amount = Money.ZERO;
	};
	
	public AbstractAccountancyEntry(Money amount) {
		this();
		this.amount = Objects.requireNonNull(amount, "amount");
	}
	
	public AbstractAccountancyEntry(Money amount, DateTime timeStamp) {
		this.amount = Objects.requireNonNull(amount, "amount");
		this.timeStamp = Objects.requireNonNull(timeStamp, "timeStamp").toDate();
	}
	
	public DateTime getTimeStamp() {
		return new DateTime(timeStamp);
	}
	
	public Money getValue() {
		return amount;
	}
}
