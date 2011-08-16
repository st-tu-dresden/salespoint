package org.salespointframework.web.helper;

import org.salespointframework.core.product.ProductInstance;
import org.salespointframework.core.product.ProductType;

// Builder API, will be awesome



// TODO Naming
public final class ATMBuilder {
	private ATMBuilder() { }
	
	public static ATMBuilder from(Iterable<ProductType> productTypes) {
		ATMBuilder atm = new ATMBuilder();
		return atm;
	}

	// SCHEISS JAVA, DRECKS GENERICS
	// awesome API versaut, Sun sucks
	/*
	public static ATMBuilder from(Iterable<ProductInstance> productTypes) {
		ATMBuilder atm = new ATMBuilder();
		return atm;
	}
	*/
	
	
	
}
