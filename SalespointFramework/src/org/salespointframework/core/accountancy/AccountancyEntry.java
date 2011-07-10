package org.salespointframework.core.accountancy;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.joda.time.DateTime;
import org.salespointframework.core.money.Money;
import org.salespointframework.util.Objects;

/**
 * This class represents an accountancy entries. The
 * <code>AccountancyEntry</code> is not intended to be directly used. Instead,
 * it should be sub-classed to define specific entry types for an accountancy,
 * for example a <code>ProductPaymentEntry</code>.
 * 
 * @author hannesweisbach
 * @author Thomas Dedek
 * 
 */
@Entity
public class AccountancyEntry implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private static final long serialVersionUID = 1L;
	@Column(name = "TimeStamp", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date timeStamp;
	
	private String accountancyType;
	private Money value;
	private String description;

	/**
	 * Protected, parameterless Constructor required by the persistence layer.
	 * Do not use it.
	 */
	@Deprecated
	protected AccountancyEntry() {
	};
	
	
	public AccountancyEntry(String accountancyType, Money value, String description) {
		this.description = Objects.requireNonNull(description, "description");
		this.value = Objects.requireNonNull(value, "value");
		this.accountancyType = Objects.requireNonNull(accountancyType, "accountancyType");
	}


	public String getAccountancyType() {
		return accountancyType;
	}

	public Money getValue() {
		return value;
	}

	public String getDescription() {
		return description;
	}

	public DateTime getTimeStamp() {
		return new DateTime(timeStamp);
	}

	public long getId() {
		return id;
	}
}
