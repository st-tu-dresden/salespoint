package org.salespointframework.core.money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import org.salespointframework.core.quantity.Metric;
import org.salespointframework.core.quantity.Quantity;
import org.salespointframework.core.quantity.rounding.RoundingStrategy;

/**
 * The <code>Money</code> class represents the Euro currency. A suitable
 * <code>Metric</code> and <code>RoundingStrategy</code> is created
 * automatically. A user-defined <code>Metric</code> can be supplied. The
 * default <code>Metric</code> is Euro. Arithmetic functions are used from the
 * <code>Quantity</code> class. The internal representation uses four digits
 * after the decimal separator.
 * 
 * @author Hannes Weisbach
 * 
 */

@SuppressWarnings("serial")
public final class Money extends Quantity {
	
	
	/**
	 * Convenience instance, used for the euro currency. The name is "Euro", the
	 * symbol is "€" and the definition is empty.
	 */
	public static final Metric EURO = new Metric("Euro", "€", "");
	
	
	/**
	 * Convenience instance, having a value of 0.
	 */
	public static final Money ZERO = new Money(0);
	
	/**
	 * Convenience instance, having a value of 1.
	 */
	public static final Money ONE = new Money(1);
	
	/**
	 * Convenience instance, having a very high value. Also an easteregg.
	 */
	public static final Money OVER9000 = new Money(9001);

	/**
	 * Creates a new <code>Money</code> instance from an amount and a metric.
	 * 
	 * @param amount
	 *            amount of the money represented by this instance
	 * @param metric
	 *            metric (currency) to be used for this instance.
	 */
	public Money(BigDecimal amount, Metric metric) {
		super(Objects.requireNonNull(amount, "amount"), Objects.requireNonNull(
				metric, "metric"), RoundingStrategy.MONETARY_ROUNDING);
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
		this(BigDecimal.valueOf(amount), Objects.requireNonNull(metric,
				"metric"));
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
		this(BigDecimal.valueOf(amount), Objects.requireNonNull(metric,
				"metric"));
	}

	/**
	 * Constructs a new <code>Money</code> object from a <code>BigDecimal</code>
	 * .
	 * 
	 * @param amount
	 *            the amount of money represented by <code>this</code>
	 */
	public Money(BigDecimal amount) {
		this(Objects.requireNonNull(amount, "amount"), Money.EURO);
	}

	/**
	 * Constructs a new <code>Money</code> object from a <code>long</code> value
	 * 
	 * @param amount
	 *            the amount of money represented by <code>this</code>
	 */
	public Money(long amount) {
		this(BigDecimal.valueOf(amount));
	}

	/**
	 * Constructs a new <code>Money</code> object from a <code>double</code>
	 * value
	 * 
	 * @param amount
	 *            the amount of money represented by <code>this</code>¸
	 */
	public Money(double amount) {
		this(BigDecimal.valueOf(amount));
	}

	/**
	 * String representation of the monetary value represented by
	 * <code>this</code>. The numerical value is truncated after two digits
	 * after the decimal separator and followed by a whitespace and the symbol
	 * stored in <code>metric</code>.
	 */
	@Override
	public String toString() {
		return amount.setScale(2, RoundingMode.HALF_UP) + " " + getMetric().getSymbol();
	}
}
