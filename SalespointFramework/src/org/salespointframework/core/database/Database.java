package org.salespointframework.core.database;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


// TODO Name ändern
// Singleton ist IMO nötig, da ALLE EntityManager von der SELBEN Factory kommen sollten
// enum Singleton pattern -> awesome
public enum Database {
	INSTANCE;
	
	private EntityManagerFactory emf;
	
	// TODO 
	// laut Doku wird keine Exception geworfen, genauer checken
	// http://download.oracle.com/javaee/6/api/javax/persistence/Persistence.html#createEntityManagerFactory(java.lang.String)
	// also gehe ich davon aus, dass da einfach null bei einem Fehler zurück kommt
	
	public boolean initializeEntityManagerFactory(String persistenceUnitName) {
		emf = Persistence.createEntityManagerFactory(persistenceUnitName);
		return emf != null ? true : false; 
	}
	
	public boolean initializeEntityManagerFactory(String persistenceUnitName, @SuppressWarnings("rawtypes") Map properties)
	{
		emf = Persistence.createEntityManagerFactory(persistenceUnitName, properties);
		return emf != null ? true : false;
	}

	public EntityManagerFactory getEntityManagerFactory() {
		return emf;
	}
	
}
