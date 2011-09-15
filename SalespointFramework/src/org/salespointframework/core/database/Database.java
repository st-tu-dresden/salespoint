package org.salespointframework.core.database;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.salespointframework.util.Objects;

// TODO Naming?

/**
 * Delegates to
 * http://download.oracle.com/javaee/6/api/javax/persistence/Persistence
 * .html#createEntityManagerFactory(java.lang.String)
 * 
 * @author Paul Henke
 * 
 */
public enum Database {
	INSTANCE;

	private EntityManagerFactory entityManagerFactory;

	/**
	 * Initialize an EntityManagerFactory for the named persistence unit.
	 * 
	 * @param persistenceUnitName
	 *            The name of the persistence unit
	 * @return
	 */
	public boolean initializeEntityManagerFactory(String persistenceUnitName)
	{
		Objects.requireNonNull(persistenceUnitName, "persistenceUnitName");
		entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
		return entityManagerFactory != null ? true : false;
	}

	/**
	 * Initialize an EntityManagerFactory for the named persistence unit.
	 * 
	 * @param persistenceUnitName
	 *            The name of the persistence unit
	 * @param properties
	 *            Additional properties to use when creating the factory. The
	 *            values of these properties override any values that may have
	 *            been configured elsewhere
	 * @return
	 */
	public boolean initializeEntityManagerFactory(String persistenceUnitName, @SuppressWarnings("rawtypes") Map properties)
	{
		Objects.requireNonNull(persistenceUnitName, "persistenceUnitName");
		Objects.requireNonNull(properties, "properties");
		entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName, properties);
		return entityManagerFactory != null ? true : false;
	}

	/**
	 * Returns the initialized EntityManagerFactory
	 * 
	 * @return The factory that creates EntityManagers configured according to
	 *         the specified persistence unit.
	 */
	public EntityManagerFactory getEntityManagerFactory()
	{
		return entityManagerFactory;
	}
}
