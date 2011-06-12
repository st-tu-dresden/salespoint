package org.salespointframework.core.money;

import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@DiscriminatorValue("METRIC")
public class Metric {
	@Id @GeneratedValue(strategy=GenerationType.AUTO) long id;
	
	private String name;
	private String symbol;
	private String definition;

	/** Default class constructor is used by JPA/Hibernate. For Object creation use
	 * parameterized public constructors instead.
	 */
	@Deprecated
	protected Metric() {};
	
	/** Class constructor specifying name, symbol and definition.
	 * 
	 * @param name       the name of the metric, for example 'meter'
	 * @param symbol     symbol used to represent the metric, for example 'm'
	 * @param definition definition of the metric, i.e. 'the base unit of length
	 *  in the International System of Units'
	 */
	public Metric(String name, String symbol, String definition) {
		this.name = name;
		this.symbol = symbol;
		this.definition = definition;
	}
	
	/** Class constructor specifying name and symbol.
	 * 
	 * @param name   the name of the metric, for example 'meter'
	 * @param symbol the symbol representing the metric, for example 'm'
	 */
	public Metric(String name, String symbol) {
		this(name, symbol, "");
	}
	
	/** Returns the name of this metric.
	 * 
	 * @return the name of this metric.
	 */
	public String getName() {
		return name;
	}
	
	/** Returns the symbol of this metric.
	 * 
	 * @return the symbol of this metric.
	 */
	public String getSymbol() {
		return symbol;
	}
	
	/** Returns the String containing the definition of this metric.
	 *  The String may be empty, but is non-null.
	 * 
	 * @return the definition of this metric.
	 */
	public String getDefinition() {
		return definition;
	}
}
