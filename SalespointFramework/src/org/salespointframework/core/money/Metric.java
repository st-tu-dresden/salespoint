package org.salespointframework.core.money;

import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Id;

@Entity
@DiscriminatorValue("<None>")
public class Metric {

	@Id
	private long id;
	
	private String name;
	private String symbol;
	private String definition;

	public String getName() {
		return name;
	}
	public String getSymbol() {
		return symbol;
	}
	public String getDefinition() {
		return definition;
	}
}
