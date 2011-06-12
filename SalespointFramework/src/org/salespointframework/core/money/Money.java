package org.salespointframework.core.money;

import javax.persistence.Entity;

@Entity
public class Money extends Quantity {

	private Currency currency;
	
	
	// TODO -> protected
	@Deprecated
	public Money() {
		
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode(); 
	}

	public Currency getCurrency() {
		return currency;
	}
}
