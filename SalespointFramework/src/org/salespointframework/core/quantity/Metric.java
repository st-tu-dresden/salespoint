package org.salespointframework.core.quantity;

import java.io.Serializable;

/**
 * Immutable metric representation. A metric consists of a name, a symbol and a
 * description.
 * 
 * @author Hannes Weisbach
 * 
 */
@SuppressWarnings("serial")
public class Metric implements Serializable {
	/**
	 * Convenience instance. This <code>Metric</code> objects is used for
	 * countable objects. The name is "Pieces", the symbol is "pcs" and the
	 * definition is empty.
	 */
	public static final Metric PIECES = new Metric("Pieces", "pcs", "");
	/**
	 * Convenience instance, which can be used for countable objects. The name
	 * is "Units", the symbol is "pcs" and the definition is empty.
	 */
	public static final Metric UNITS = new Metric("Units", "pcs", "");
	/**
	 * Convenience instance, used for the euro currency. The name is "Euro", the
	 * symbol is "€" and the definition is empty.
	 */
	public static final Metric EURO = new Metric("Euro", "€", "");

	private String name;
	private String symbol;
	private String definition;

	/**
	 * Class constructor specifying name, symbol and definition.
	 * 
	 * @param name
	 *            the name of the metric, for example 'meter'
	 * @param symbol
	 *            symbol used to represent the metric, for example 'm'
	 * @param definition
	 *            definition of the metric, i.e. 'the base unit of length in the
	 *            International System of Units'
	 */
	public Metric(String name, String symbol, String definition) {
		this.name = name;
		this.symbol = symbol;
		this.definition = definition;
	}

	/**
	 * Class constructor specifying name and symbol.
	 * 
	 * @param name
	 *            the name of the metric, for example 'meter'
	 * @param symbol
	 *            the symbol representing the metric, for example 'm'
	 */
	public Metric(String name, String symbol) {
		this(name, symbol, "");
	}

	/**
	 * Returns the name of this metric.
	 * 
	 * @return the name of this metric.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the symbol of this metric.
	 * 
	 * @return the symbol of this metric.
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * Returns the String containing the definition of this metric. The String
	 * may be empty, but is non-null.
	 * 
	 * @return the definition of this metric.
	 */
	public String getDefinition() {
		return definition;
	}

	/**
	 * Compares this <code>Metric</code> to <code>obj</code>.
	 * 
	 * Returns <code>false</code> if <code>obj</code> is not an instance of
	 * <code>Metric</code>. Otherwise, two <code>Metric</code>s are equal, if
	 * their symbols and names are equal.
	 * 
	 * @param obj
	 *            the Object to which <code>this</code> is compared.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof Metric))
			return false;
		else {
			Metric m = (Metric) obj;
			return symbol.equals(m.symbol) && name.equals(m.name);
		}
	}
	// TODO overwrite hashcode
	// TODO toString
}
