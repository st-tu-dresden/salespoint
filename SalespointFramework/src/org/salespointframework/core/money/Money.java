package org.salespointframework.core.money;

import java.math.BigDecimal;

import javax.persistence.Entity;

import org.salespointframework.core.quantity.Metric;
import org.salespointframework.core.quantity.Quantity;
import org.salespointframework.core.quantity.rounding.RoundDownStrategy;

/**
 * The <code>Money</code> class represents the Euro currency. A suitable
 * <code>Metric</code> and <code>RoundingStrategy</code> is created
 * automatically. A user-defined <code>Metric</code> can be supplied. The
 * default <code>Metric</code> is Euro. Arithmetic functions are used from the
 * <code>Quantity</code> class. The internal representation uses four digits
 * after the decimal separator.
 * 
 * @author hannesweisbach
 * 
 */
@Entity
public class Money extends Quantity {
	public static final Money ZERO = new Money(0);

	{
		metric = new Metric("Euro", "€", "");
		roundingStrategy = new RoundDownStrategy(4);
	}

	/**
	 * Parameterless constructor, do not use. This constructor is required by
	 * the persistence layer and may not be used by application developers.
	 */
	@Deprecated
	protected Money() {
	}

	/**
	 * Creates a new <code>Money</code> instance from an amount and a metric.
	 * 
	 * @param amount
	 *            amount of the money represented by this instance
	 * @param metric
	 *            metric (currency) to be used for this instance.
	 */
	public Money(BigDecimal amount, Metric metric) {
		if (metric != null)
			this.metric = metric;
		this.amount = amount;
	}

	/**
	 * Creates a new <code>Money</code> instance from an amount and a metric.
	 * 
	 * @param amount
	 *            amount of the money represented by this instance
	 * @param metric
	 *            metric (currency) to be used for this instance.
	 */
	public Money(int amount, Metric metric) {
		this(BigDecimal.valueOf(amount), metric);
	}

	/**
	 * Creates a new <code>Money</code> instance from an amount and a metric.
	 * 
	 * @param amount
	 *            amount of the money represented by this instance
	 * @param metric
	 *            metric (currency) to be used for this instance.
	 */
	public Money(double amount, Metric metric) {
		this(BigDecimal.valueOf(amount), metric);
	}

	/**
	 * Constructs a new <code>Money</code> object from a <code>BigDecimal</code>
	 * .
	 * 
	 * @param amount
	 *            the amount of money represented by <code>this</code>
	 */
	public Money(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * Constructs a new <code>Money</code> object from a <code>long</code> value
	 * 
	 * @param amount
	 *            the amount of money represented by <code>this</code>
	 */
	public Money(long amount) {
		this.amount = BigDecimal.valueOf(amount);
	}

	/**
	 * Constructs a new <code>Money</code> object from a <code>double</code>
	 * value
	 * 
	 * @param amount
	 *            the amount of money represented by <code>this</code>¸
	 */
	public Money(double amount) {
		this.amount = BigDecimal.valueOf(amount);
	}

	/**
	 * String representation of the monetary value represented by
	 * <code>this</code>. The numerical value is truncated after two digits
	 * after the decimal separator and followed by a whitespace and the symbol
	 * stored in <code>metric</code>.
	 */
	public String toString() {
		return amount.setScale(2) + " " + metric.getSymbol();
	}
}
