package org.salespointframework.core.money;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Quantity implements Comparable<Quantity> {

	@Id @GeneratedValue(strategy=GenerationType.AUTO) long id;
	
	private float amount;
	private Metric metric;
	private RoundingStrategy roundingStrategy;
	
	/** Protected class constructor is required for JPA/Hibernate.
	 * Use parameterized Constructor instead.
	 */
	protected Quantity() {};
	
	protected Quantity(float amount, Metric metric, RoundingStrategy roundingStrategy) {
		this.amount = amount;
		this.metric = metric;
		this.roundingStrategy = roundingStrategy;
	}
	
	public Metric getMetric() {
		return metric;
	}

	@Override
	public int compareTo(Quantity o) {
		return Float.compare(amount, o.amount);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Quantity))
			return false;
		else
		//FIXME 
		return amount == ((Quantity)obj).amount;
	}
	
	@Override
	public String toString() {
		return amount + metric.getSymbol();
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	public Quantity add(Quantity quantity) {
		amount += quantity.amount;
		return this;
	}
	
	public Quantity subtract(Quantity quantity) {
		amount -= quantity.amount;
		return this;
	}
	
	public Quantity multiply(Quantity quantity) {
		amount *= quantity.amount;
		return this;
	}
	
	public Quantity divide(Quantity quantity) {
		amount /= quantity.amount;
		return this;
	}

	public float getAmount() {
		return amount;
	}
	
	public void setAmount(float amount) {
		this.amount = amount;
	}
}
