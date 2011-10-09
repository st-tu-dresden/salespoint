package org.salespointframework.web;

import org.salespointframework.core.database.Database;


/**
 * 
 * @author Paul Henke
 *
 */
public class PersistenceUnitInitializer {

	//private String persistenceUnitName;
	
	public PersistenceUnitInitializer() {
		
	}
	
	public void setPersistenceUnitName(String persistenceUnitName) {
		//this.persistenceUnitName = persistenceUnitName;
		Database.INSTANCE.initializeEntityManagerFactory(persistenceUnitName);
	}
}
