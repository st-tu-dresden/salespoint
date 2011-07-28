package org.salespointframework.core.quantity;

import java.io.Serializable;
import java.math.BigDecimal;

import org.salespointframework.core.quantity.rounding.RoundDownStrategy;
import org.salespointframework.core.quantity.rounding.RoundingStrategy;

/**
 * The Quantity class represents an amount of something. 'Something' is
 * specified using a <code>Metric</code>. A <code>RoundingStrategy</code>
 * accommodates amounts that do not fit the value set of a given metric (Think
 * of money: 0.0001€ has no meaning in the real world). The
 * <code>roundingStrategy</code> is applied to <code>amount</code> in the class
 * constructor. This way, every instance has a valid <code>amount</code>.
 * 
 * To allow arithmetic operations on
 * <code>Quantity<code> objects and instances of subclasses of <code>Quantity</code>
 * to return the correct type (and thus avoiding casts), <code>Quantity</code>
 * instances are immutable and all subclasses of <code>Quantity</code> have to
 * be immutable.
 * 
 * @author hannesweisbach
 * 
 */

public class Quantity implements Comparable<Quantity>, Serializable {

	public static final RoundingStrategy ROUND_ONE = new RoundDownStrategy(0);
	/**
	 * 
	 */
	private static final long serialVersionUID = -5292263711685595615L;
	// immutable
	protected BigDecimal amount;
	// immutable
	protected Metric metric;
	// immutable
	protected RoundingStrategy roundingStrategy;

	/**
	 * Protected class constructor is required for JPA/Hibernate. Use
	 * parameterized Constructor instead.
	 */
	// protected Quantity() {
	// };

	/**
	 * Parameterized class constructor. <code>amount</code> is immediately
	 * rounded using the supplied <code>roundingStrategy</code>.
	 * 
	 * @param amount
	 *            <code>amount</code> represented by this <code>Quantity</code>
	 * @param metric
	 *            <code>metric</code> used for this <code>Quantity</code>
	 * @param roundingStrategy
	 *            <code>roundingStrategy</code> to be used with this
	 *            <code>Quantity</code>
	 */
	public Quantity(BigDecimal amount, Metric metric,
			RoundingStrategy roundingStrategy) {
		this.amount = roundingStrategy.round(amount);
		this.metric = metric;
		this.roundingStrategy = roundingStrategy;
	}

	/**
	 * Translates an <code>int</code> to a <code>Quantity</code>.
	 * 
	 * The Integer is rounded according to the supplied
	 * <code>RoudingStrategy</code>.
	 * 
	 * @param amount
	 *            <code>int</code> value to be converted to
	 *            <code>Quantity</code>
	 * @param metric
	 *            <code>metric</code> to be used with this <code>Quantity</code>
	 * @param roundingStrategy
	 *            <code>roudingStrategy</code> to be used with this
	 *            <code>Quantity</code>
	 */
	public Quantity(int amount, Metric metric, RoundingStrategy roundingStrategy) {
		this.amount = roundingStrategy.round(new BigDecimal(amount));
		this.metric = metric;
		this.roundingStrategy = roundingStrategy;
	}

	/**
	 * Translates a <code>long</code> to a <code>Quantity</code>.
	 * 
	 * @param amount
	 *            <code>long</code> value to be converted to a
	 *            <code>Quantity</code>
	 * @param metric
	 *            <code>metric</code> to be used with this <code>Quantity</code>
	 * @param roundingStrategy
	 *            <code>roundingStrategy</code> to be used with this
	 *            <code>Quantity</code>
	 */
	public Quantity(long amount, Metric metric,
			RoundingStrategy roundingStrategy) {
		this.amount = roundingStrategy.round(new BigDecimal(amount));
		this.metric = metric;
		this.roundingStrategy = roundingStrategy;
	}

	/**
	 * Translates a <code>float</code> to a <code>Quantity</code>.
	 * 
	 * @param amount
	 *            <code>float</code> value to be converted to a
	 *            <code>Quantity</code>
	 * @param metric
	 *            <code>metric</code> to be used with this <code>Quantity</code>
	 * @param roundingStrategy
	 *            <code>roundingStrategy</code> to be used with this
	 *            <code>Quantity</code>
	 */
	public Quantity(float amount, Metric metric,
			RoundingStrategy roundingStrategy) {
		this.amount = roundingStrategy.round(new BigDecimal(amount));
		this.metric = metric;
		this.roundingStrategy = roundingStrategy;
	}

	/**
	 * Translates a <code>double</code> to a <code>Quantity</code>.
	 * 
	 * @param amount
	 *            <code>double</code> value to be converted to a
	 *            <code>Quantity</code>
	 * @param metric
	 *            <code>metric</code> to be used with this <code>Quantity</code>
	 * @param roundingStrategy
	 *            <code>roundingStrategy</code> to be used with this
	 *            <code>Quantity</code>
	 */
	public Quantity(double amount, Metric metric,
			RoundingStrategy roundingStrategy) {
		this.amount = roundingStrategy.round(new BigDecimal(amount));
		this.metric = metric;
		this.roundingStrategy = roundingStrategy;
	}

	/**
	 * Returns the <code>metric</code> associated with this
	 * <code>Quantity</code>.
	 * 
	 * @return <code>Metric</code> of this <code>Quantity</code>.
	 */
	public Metric getMetric() {
		return metric;
	}

	/**
	 * Returns the <code>RoundingStrategy</code> of this <code>Quantity</code>.
	 * 
	 * @return <code>RoundingStrategy</code> of this <code>Quantity</code>.
	 */
	public RoundingStrategy getRoundingStrategy() {
		return roundingStrategy;
	}

	/**
	 * Returns the <code>amount</code> of this as <code>float</code>.
	 * 
	 * @return the <code>amount</code> of this as <code>float</code>.
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	@Override
	public int compareTo(Quantity o) {
		return amount.compareTo(o.amount);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Quantity))
			return false;
		else {
			Quantity q = (Quantity) obj;
			return roundingStrategy.equals(q.roundingStrategy)
					&& amount.equals(q.amount) && metric.equals(metric);
		}
	}

	@Override
	public String toString() {
		return amount + metric.getSymbol();
	}

	@Override
	public int hashCode() {
		return amount.hashCode();
	}

	/**
	 * Sums two quantities up. The <code>amount</code> of this and quantity are
	 * added, and a new <code>Quantity</code>-instance is returned, representing
	 * the sum. The metric and rounding Strategy of this are used for the new
	 * instance. No check is performed, whether the metrics of both quantities
	 * are compatible, i.e. it is possible to combine, say kg and s.
	 * 
	 * @param quantity
	 *            <code>Quantity</code> to be added to this.
	 * @return a new <code>Quantity</code> object representing the sum of this
	 *         and <code>quantity</code>.
	 */
	public Quantity add(Quantity quantity) {
		return new Quantity(amount.add(quantity.amount), metric,
				roundingStrategy);
	}

	/**
	 * Subtracts a quantity from this. The <code>amount</code> of this is
	 * subtracted by that of quantity, and a new <code>Quantity</code>-instance
	 * is returned, representing the difference. The metric and rounding
	 * Strategy of this are used for the new instance. No check is performed,
	 * whether the metrics of both quantities are compatible, i.e. it is
	 * possible to combine, say kg and s.
	 * 
	 * @param quantity
	 *            <code>Quantity</code> to be subtracted from this.
	 * @return a new <code>Quantity</code> object representing the difference of
	 *         this and <code>quantity</code>.
	 */
	public Quantity subtract(Quantity quantity) {
		return new Quantity(amount.subtract(quantity.amount), metric,
				roundingStrategy);
	}

	public Quantity clone() {
		Quantity q;
		q = this.clone();
		return q;
	}

	
	/**
	 * Multiplies two quantities. The <code>amount</code> of this and quantity
	 * are multiplied, and a new <code>Quantity</code>-instance is returned,
	 * representing the product. The metric and rounding Strategy of this are
	 * used for the new instance. No check is performed, whether the metrics of
	 * both quantities are compatible, i.e. it is possible to combine, say kg
	 * and s.
	 * 
	 * @param quantity
	 *            <code>Quantity</code> to be multiplied with this.
	 * @return a new <code>Quantity</code> object representing the product of
	 *         this and <code>quantity</code>.
	 */
	public Quantity multiply(Quantity quantity) {
		return new Quantity(amount.multiply(quantity.amount), metric,
				roundingStrategy);
	}

	public <T extends Quantity> T multiply_(T quantity) {
		@SuppressWarnings("unchecked")
		T q = (T) quantity.clone();
		q.amount = roundingStrategy.round(amount.multiply(quantity.amount));
		return q;
	}

	/**
	 * Divides this by another quantity. The <code>amount</code> of this the
	 * dividend, <code>quantity</code> the divisor, and a new
	 * <code>Quantity</code>-instance is returned as quotient. The metric and
	 * rounding Strategy of this are used for the new instance. No check is
	 * performed, whether the metrics of both quantities are compatible, i.e. it
	 * is possible to combine, say kg and s.
	 * 
	 * @param quantity
	 *            <code>Quantity</code> to be used as divisor.
	 * @return a new <code>Quantity</code> object representing the quotient.
	 */
	public Quantity divide(Quantity quantity) {
		return new Quantity(amount.divide(quantity.amount), metric,
				roundingStrategy);
	}

}
