package org.salespointframework.core.time;

import org.joda.time.DateTime;


// Singleton, find ich sinnvoll, weiter füllen! 
// find ich doch nicht mehr sinnvoll
public enum ShopTime {
	INSTANCE;
	
	@Deprecated // Use Shop.INSTANCE.getTime().getDateTime() instead
	public DateTime getDateTime() {
		return new DateTime();
	}
	
}
