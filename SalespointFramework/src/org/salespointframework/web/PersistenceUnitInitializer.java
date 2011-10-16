package org.salespointframework.web;

import org.salespointframework.core.database.Database;


/**
 * 
 * @author Paul Henke
 *
 */
public class PersistenceUnitInitializer {

	public PersistenceUnitInitializer() {
		
	}
	
	public void setPersistenceUnitName(String persistenceUnitName) {
		//this.persistenceUnitName = persistenceUnitName;
		boolean result = Database.INSTANCE.initializeEntityManagerFactory(persistenceUnitName);
		if(!result) {
			throw new IllegalArgumentException("The PersistentUnit: " + persistenceUnitName + " does not exist!");
		}
	}
}
