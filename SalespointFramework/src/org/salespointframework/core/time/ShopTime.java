package org.salespointframework.core.time;

import org.joda.time.DateTime;


// Singleton, find ich sinnvoll, weiter füllen! 
public enum ShopTime {
	INSTANCE;
	
	public DateTime getDateTime() {
		return new DateTime();
	}
	
}
