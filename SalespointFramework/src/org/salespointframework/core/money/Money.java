package org.salespointframework.core.money;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Money extends Quantity {

	private Currency currency;
	
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
}
