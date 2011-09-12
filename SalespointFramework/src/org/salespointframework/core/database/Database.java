package org.salespointframework.core.database;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.salespointframework.util.Objects;


// TODO Name ändern
// Singleton ist IMO nötig, da ALLE EntityManager von der SELBEN Factory kommen sollten
// enum Singleton pattern -> awesome
/**
 * 
 * @author Paul Henke
 * 
 */
public enum Database {
	INSTANCE;
	
	private EntityManagerFactory emf;
	
	// TODO 
	// laut Doku wird keine Exception geworfen, genauer checken
	// http://download.oracle.com/javaee/6/api/javax/persistence/Persistence.html#createEntityManagerFactory(java.lang.String)
	// also gehe ich davon aus, dass da einfach null bei einem Fehler zurück kommt
	
	public boolean initializeEntityManagerFactory(String persistenceUnitName) {
		Objects.requireNonNull(persistenceUnitName, "persistenceUnitName");
		emf = Persistence.createEntityManagerFactory(persistenceUnitName);
		return emf != null ? true : false; 
	}
	
	public boolean initializeEntityManagerFactory(String persistenceUnitName, @SuppressWarnings("rawtypes") Map properties)
	{
		Objects.requireNonNull(persistenceUnitName, "persistenceUnitName");
		Objects.requireNonNull(properties, "properties");
		emf = Persistence.createEntityManagerFactory(persistenceUnitName, properties);
		return emf != null ? true : false;
	}

	public EntityManagerFactory getEntityManagerFactory() {
		return emf;
	}
	
}
